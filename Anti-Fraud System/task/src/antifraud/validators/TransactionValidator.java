package antifraud.validators;

import antifraud.exceptions.*;
import antifraud.models.Transaction;
import antifraud.models.TransactionFeedback;
import antifraud.utilities.TransactionState;
import antifraud.utilities.WorldRegion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TransactionValidator {

    @Autowired @Qualifier("ipv4AddressValidator")
    private IpAddressValidator ipAddressValidator;

    @Autowired @Qualifier("luhnAlgorithm")
    private CardNumberValidator cardNumberValidator;

    @Autowired @Qualifier("iso8601FormatValidator")
    private DateFormatValidator dateFormatValidator;

    public void validateTransaction(Transaction transaction) throws InvalidIpAddressException, InvalidCardNumberException, InvalidRegionException, InvalidDateFormatException {
        if (Objects.isNull(transaction)) throw new IllegalArgumentException("invalid transaction request");
        ipAddressValidator.validate(transaction.getIpAddress());
        cardNumberValidator.validate(transaction.getCardNumber());
        dateFormatValidator.validate(transaction.getDate());
        if (Objects.isNull(WorldRegion.getNullableWorldRegionByString(transaction.getWorldRegion()))) throw new InvalidRegionException();
    }

    public void validateTransactionFeedback(TransactionFeedback transactionFeedback) throws InvalidTransactionStateException {
        if (Objects.isNull(TransactionState.getNullableTransactionStateByString(transactionFeedback.getFeedback())))
            throw new InvalidTransactionStateException("invalid feedback value");
    }
}
