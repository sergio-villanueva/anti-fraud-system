package antifraud.validators;

import antifraud.exceptions.InvalidCardNumberException;

public interface CardNumberValidator {
    /** Validate if the given card number has the correct card format
     * @param cardNumber the card number to validate
     * @exception InvalidCardNumberException thrown if the card number has incorrect format or is null
     * */
    void validate(String cardNumber) throws InvalidCardNumberException;
}
