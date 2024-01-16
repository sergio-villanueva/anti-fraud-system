package antifraud.services;

import antifraud.database.entities.TransactionEntity;
import antifraud.database.entities.TransactionLimitsEntity;
import antifraud.database.repositories.StolenPaymentCardRepository;
import antifraud.database.repositories.SuspiciousIpAddressRepository;
import antifraud.database.repositories.TransactionLimitsRepository;
import antifraud.database.repositories.TransactionRepository;
import antifraud.models.Transaction;
import antifraud.utilities.WorldRegion;
import antifraud.utilities.TransactionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class FraudulentTransactionsServiceImpl implements FraudulentTransactionsService {

    @Autowired
    private SuspiciousIpAddressRepository suspiciousIpAddressRepository;

    @Autowired
    private StolenPaymentCardRepository stolenPaymentCardRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionLimitsRepository transactionLimitsRepository;

    private static final int pageSize = 3;

    private static final int hours = 1; // used to check last <hours> in transaction db calls

    private final Logger logger = LoggerFactory.getLogger(FraudulentTransactionsServiceImpl.class);

    /** Verify the transaction
     * @param transaction the transaction to be verified for fraud
     * @return state after a transaction has been verified
     * */
    @Override
    public TransactionState verify(Transaction transaction) {

        // determine if the transaction is blacklisted
        boolean suspiciousInd = suspiciousIpAddressRepository.existsByIpAddress(transaction.getIpAddress());
        logger.debug("ip address is suspicious: " + suspiciousInd);
        boolean stolenInd = stolenPaymentCardRepository.existsByCardNumber(transaction.getCardNumber());
        logger.debug("card is stolen: " + stolenInd);
        // determine if there is regional fraud
        int fraudRegionInd = findFraudRegionInd(transaction);
        // determine if there is ip fraud
        int fraudIpInd = findFraudIpInd(transaction);

        // find transaction limits
        TransactionLimitsEntity limitsEntity = findTransactionLimits(transaction);

        // determine status
        TransactionState state = findStatus(suspiciousInd, stolenInd, transaction.getAmount(), fraudRegionInd, fraudIpInd, limitsEntity);
        logger.info("transaction state: " + state);
        // determine reasons
        List<String> reasons = findReasons(suspiciousInd, stolenInd, transaction.getAmount(), state, fraudRegionInd, fraudIpInd, limitsEntity);
        logger.info("reasons for transaction state: " + reasons);

        // set reasons for determining transaction state
        state.setReasons(reasons);
        return state;
    }

    private TransactionLimitsEntity findTransactionLimits(Transaction transaction) {
        Optional<TransactionLimitsEntity> optional = transactionLimitsRepository.findByCardNumber(transaction.getCardNumber());
        return optional.orElseGet(() -> {
            // if this is a new card number, use the default limits
            return TransactionLimitsEntity.builder()
                    .cardNumber(transaction.getCardNumber())
                    .maxAllowedLimit(200L)
                    .maxManualLimit(1500L)
                    .build();
        });
    }

    private int findFraudIpInd(Transaction transaction) {
        String ipAddress = transaction.getIpAddress();
        LocalDateTime now = LocalDateTime.parse(transaction.getDate());
        LocalDateTime hourAgo = now.minusHours(hours);
        Pageable pageable = PageRequest.of(0, pageSize);

        Set<String> ipAddressSet = Set.copyOf(transactionRepository
                .findDistinctIpAddressesByCardNumberAndDateRange(transaction.getCardNumber(), hourAgo, now, pageable));
        return ipAddressSet.contains(ipAddress) ? ipAddressSet.size() : ipAddressSet.size() + 1;
    }

    private int findFraudRegionInd(Transaction transaction) {
        WorldRegion worldRegion = WorldRegion.getNullableWorldRegionByString(transaction.getWorldRegion());
        LocalDateTime now = LocalDateTime.parse(transaction.getDate());
        LocalDateTime hourAgo = now.minusHours(hours);
        Pageable pageable = PageRequest.of(0, pageSize);

        Set<WorldRegion> worldRegionSet = Set.copyOf(transactionRepository
                .findDistinctWorldRegionsByCardNumberAndDateRange(transaction.getCardNumber(), hourAgo, now, pageable));
        return worldRegionSet.contains(worldRegion) ? worldRegionSet.size() : worldRegionSet.size() + 1;
    }

    @Deprecated
    private void save(Transaction transaction, TransactionState state, List<String> reasons, boolean suspiciousInd, boolean stolenInd) {

        TransactionEntity transactionEntity = TransactionEntity.builder()
                .amount(transaction.getAmount())
                .cardNumber(transaction.getCardNumber())
                .ipAddress(transaction.getIpAddress())
                .date(LocalDateTime.parse(transaction.getDate()))
                .info(reasons)
                .worldRegion(WorldRegion.getNullableWorldRegionByString(transaction.getWorldRegion()))
                .result(state)
                .build();

        stolenPaymentCardRepository.findByCardNumber(transaction.getCardNumber()).ifPresent((stolenPaymentCardEntity) -> {
            stolenPaymentCardEntity.getTransactionEntities().add(transactionEntity); // save child in parent
            transactionEntity.setStolenPaymentCardEntity(stolenPaymentCardEntity); // save parent in child
            stolenPaymentCardRepository.save(stolenPaymentCardEntity);
        });

        suspiciousIpAddressRepository.findByIpAddress(transaction.getIpAddress()).ifPresent((suspiciousIpAddressEntity) -> {
            suspiciousIpAddressEntity.getTransactionEntities().add(transactionEntity); // save child in parent
            transactionEntity.setSuspiciousIpAddressEntity(suspiciousIpAddressEntity); // save parent in child
            suspiciousIpAddressRepository.save(suspiciousIpAddressEntity);
        });

        if (!stolenInd && !suspiciousInd) {
            transactionRepository.save(transactionEntity); // insert non-blacklisted transaction
        }

    }

    private TransactionState findStatus(boolean suspiciousInd, boolean stolenInd, long amount, int fraudRegionInd, int fraudIpInd, TransactionLimitsEntity limitsEntity) {
        if (suspiciousInd || stolenInd) return TransactionState.PROHIBITED;

        if ((fraudRegionInd > 3) || (fraudIpInd > 3)) return TransactionState.PROHIBITED;
        if ((fraudRegionInd == 3) || (fraudIpInd == 3)) return TransactionState.MANUAL_PROCESSING;

        if (amount <= 0)
            throw new IllegalArgumentException("amount cannot be negative");
        else if (amount <= limitsEntity.getMaxAllowedLimit())
            return TransactionState.ALLOWED;
        else if (amount <= limitsEntity.getMaxManualLimit())
            return TransactionState.MANUAL_PROCESSING;
        else
            return TransactionState.PROHIBITED;
    }

    private List<String> findReasons(boolean suspiciousInd, boolean stolenInd, long amount, TransactionState state, int fraudRegionInd, int fraudIpInd, TransactionLimitsEntity limitsEntity) {
        List<String> reasons = new ArrayList<>();

        switch (state) {
            case ALLOWED:
                reasons.add("none");
                break;
            case MANUAL_PROCESSING:
                if (amount > limitsEntity.getMaxAllowedLimit() && amount <= limitsEntity.getMaxManualLimit()) reasons.add("amount");
                if (fraudIpInd == 3) reasons.add("ip-correlation");
                if (fraudRegionInd == 3) reasons.add("region-correlation");
                break;
            case PROHIBITED:
                if (amount > limitsEntity.getMaxManualLimit()) reasons.add("amount");
                if (stolenInd) reasons.add("card-number");
                if (suspiciousInd) reasons.add("ip");
                if (fraudIpInd > 3) reasons.add("ip-correlation");
                if (fraudRegionInd > 3) reasons.add("region-correlation");
                break;
        }
        return reasons;
    }

}
