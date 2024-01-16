package antifraud.services;

import antifraud.database.entities.TransactionEntity;
import antifraud.database.entities.TransactionLimitsEntity;
import antifraud.database.repositories.StolenPaymentCardRepository;
import antifraud.database.repositories.SuspiciousIpAddressRepository;
import antifraud.database.repositories.TransactionLimitsRepository;
import antifraud.database.repositories.TransactionRepository;
import antifraud.exceptions.CardNumberDoesNotExistException;
import antifraud.exceptions.FeedbackAlreadyExistsException;
import antifraud.exceptions.SameResultAndFeedbackException;
import antifraud.exceptions.TransactionNotFoundException;
import antifraud.models.Transaction;
import antifraud.models.TransactionDTO;
import antifraud.models.TransactionFeedback;
import antifraud.utilities.TransactionState;
import antifraud.utilities.WorldRegion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class TransactionService {

    @Autowired
    private SuspiciousIpAddressRepository suspiciousIpAddressRepository;

    @Autowired
    private StolenPaymentCardRepository stolenPaymentCardRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionLimitsRepository transactionLimitsRepository;

    @Autowired
    private TransactionLimitService transactionLimitService;


    /** Creates a transaction in the database
     * @param transaction the transaction to record
     * @param state the result of transaction verification
     * */
    @Transactional
    public void createTransaction(Transaction transaction, TransactionState state) {

        // save the current limits with the new associated transaction
        TransactionEntity transactionEntity = transactionRepository.save(TransactionEntity.builder()
                .amount(transaction.getAmount())
                .cardNumber(transaction.getCardNumber())
                .ipAddress(transaction.getIpAddress())
                .date(LocalDateTime.parse(transaction.getDate()))
                .info(state.getReasons())
                .worldRegion(WorldRegion.getNullableWorldRegionByString(transaction.getWorldRegion()))
                .result(state)
                .build()); // save transaction beforehand to generate transaction id

        TransactionLimitsEntity limitsEntity = transactionLimitsRepository.findByCardNumber(transaction.getCardNumber())
                .orElseGet(() -> TransactionLimitsEntity.builder()
                        .cardNumber(transaction.getCardNumber())
                        .maxAllowedLimit(200L)
                        .maxManualLimit(1500L)
                        .build());

        limitsEntity.getTransactionEntities().add(transactionEntity);
        transactionEntity.setTransactionLimitsEntity(limitsEntity);
        transactionLimitsRepository.save(limitsEntity);

        // saves the stolen card with the new associated transaction
        stolenPaymentCardRepository.findByCardNumber(transaction.getCardNumber()).ifPresent((stolenPaymentCardEntity) -> {
            System.out.println("before stolen save");
            stolenPaymentCardEntity.getTransactionEntities().add(transactionEntity); // save child in parent
            transactionEntity.setStolenPaymentCardEntity(stolenPaymentCardEntity); // save parent in child
            stolenPaymentCardRepository.save(stolenPaymentCardEntity);
            System.out.println("after stolen save");
        });

        // saves the suspicious ip with the new associated transaction
        suspiciousIpAddressRepository.findByIpAddress(transaction.getIpAddress()).ifPresent((suspiciousIpAddressEntity) -> {
            System.out.println("before suspicious save");
            suspiciousIpAddressEntity.getTransactionEntities().add(transactionEntity); // save child in parent
            transactionEntity.setSuspiciousIpAddressEntity(suspiciousIpAddressEntity); // save parent in child
            suspiciousIpAddressRepository.save(suspiciousIpAddressEntity);
            System.out.println("after suspicious save");
        });

    }

    /** Update the feedback and transaction limits used for fraud verification
     * @param transactionFeedback the feedback to record
     * @exception FeedbackAlreadyExistsException thrown if the transaction already had feedback
     * */
    public TransactionDTO updateTransactionFeedback(TransactionFeedback transactionFeedback) throws FeedbackAlreadyExistsException, SameResultAndFeedbackException {

        TransactionEntity transactionEntity = transactionRepository.findById(transactionFeedback.getTransactionId()).orElseThrow(() -> new TransactionNotFoundException("invalid transaction id"));
        // check if feedback already exists
        if (Objects.nonNull(transactionEntity.getFeedback())) throw new FeedbackAlreadyExistsException();
        // add feedback to the associated transaction
        TransactionState feedback = TransactionState.getNullableTransactionStateByString(transactionFeedback.getFeedback());
        transactionEntity.setFeedback(feedback);
        // update the current limits based on current transaction status and feedback
        TransactionLimitsEntity limitsEntity = transactionEntity.getTransactionLimitsEntity();
        updateTransactionLimits(transactionEntity.getResult(), feedback, transactionEntity, limitsEntity);

        transactionLimitsRepository.save(limitsEntity);
        return toDTO(transactionEntity);
    }

    public List<TransactionDTO> retrieveTransactionHistory() {
        List<TransactionDTO> dtoList = transactionRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream().map(this::toDTO).toList();
        dtoList.forEach(System.out::println);
        return dtoList;
    }

    public List<TransactionDTO> retrieveTransactionHistory(String cardNumber) {
        List<TransactionDTO> dtoList = transactionRepository.findByCardNumber(cardNumber)
                .stream().map(this::toDTO).toList();
        if (dtoList.isEmpty()) throw new CardNumberDoesNotExistException();
        return dtoList;
    }

    private TransactionDTO toDTO(TransactionEntity transactionEntity) {

        return TransactionDTO.builder()
                .transactionId(transactionEntity.getId())
                .amount(transactionEntity.getAmount())
                .ipAddress(transactionEntity.getIpAddress())
                .cardNumber(transactionEntity.getCardNumber())
                .worldRegion(transactionEntity.getWorldRegion())
                .date(transactionEntity.getDate())
                .result(transactionEntity.getResult())
                .feedback(transactionEntity.getFeedback())
                .build();
    }

    private void updateTransactionLimits(TransactionState result, TransactionState feedback, TransactionEntity transactionEntity, TransactionLimitsEntity limitsEntity) throws SameResultAndFeedbackException {
        long maxAllowed = limitsEntity.getMaxAllowedLimit();
        long maxManual = limitsEntity.getMaxManualLimit();

        switch (result) {
            // original state: ALLOWED
            case ALLOWED:
                switch (feedback) {
                    // new state: ALLOWED
                    case ALLOWED:
                        throw new SameResultAndFeedbackException();
                    // new state: MANUAL_PROCESSING
                    case MANUAL_PROCESSING:
                        maxAllowed = transactionLimitService.decreaseLimit(transactionEntity.getAmount(), maxAllowed);
                        break;
                    // new state: PROHIBITED
                    case PROHIBITED:
                        maxAllowed = transactionLimitService.decreaseLimit(transactionEntity.getAmount(), maxAllowed);
                        maxManual = transactionLimitService.decreaseLimit(transactionEntity.getAmount(), maxManual);
                        break;
                }
                break;
            // original state: MANUAL_PROCESSING
            case MANUAL_PROCESSING:
                switch (feedback) {
                    // new state: ALLOWED
                    case ALLOWED:
                        maxAllowed = transactionLimitService.increaseLimit(transactionEntity.getAmount(), maxAllowed);
                        break;
                    // new state: MANUAL_PROCESSING
                    case MANUAL_PROCESSING:
                        throw new SameResultAndFeedbackException();
                    // new state: PROHIBITED
                    case PROHIBITED:
                        maxManual = transactionLimitService.decreaseLimit(transactionEntity.getAmount(), maxManual);
                        break;
                }
                break;
            // original state: PROHIBITED
            case PROHIBITED:
                switch (feedback) {
                    // new state: ALLOWED
                    case ALLOWED:
                        maxAllowed = transactionLimitService.increaseLimit(transactionEntity.getAmount(), maxAllowed);
                        maxManual = transactionLimitService.increaseLimit(transactionEntity.getAmount(), maxManual);
                        break;
                    // new state: MANUAL_PROCESSING
                    case MANUAL_PROCESSING:
                        maxManual = transactionLimitService.increaseLimit(transactionEntity.getAmount(), maxManual);
                        break;
                    // new state: PROHIBITED
                    case PROHIBITED:
                        throw new SameResultAndFeedbackException();
                }
                break;
        }

        limitsEntity.setMaxAllowedLimit(maxAllowed);
        limitsEntity.setMaxManualLimit(maxManual);
    }

}
