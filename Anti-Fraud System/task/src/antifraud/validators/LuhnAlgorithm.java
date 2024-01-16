package antifraud.validators;

import antifraud.exceptions.InvalidCardNumberException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("luhnAlgorithm")
public class LuhnAlgorithm implements CardNumberValidator{
    /**
     * Validate if the given card number has the correct card format
     *
     * @param cardNumber the card number to validate
     * @exception InvalidCardNumberException thrown if the card number has incorrect format or is null
     */
    @Override
    public void validate(String cardNumber) throws InvalidCardNumberException {
        if (Objects.isNull(cardNumber)) throw new InvalidCardNumberException("card number is null");
        if (cardNumber.length() != 16) throw new InvalidCardNumberException();
        if (!cardNumber.matches("[0-9]+")) throw new InvalidCardNumberException();
        if (!runAlgorithm(cardNumber)) throw new InvalidCardNumberException();
    }

    private boolean runAlgorithm(String cardNumber) {
        int i = cardNumber.length();
        int j = 1;
        boolean doubleInd = false;
        int sum = 0;

        for (int k = i - j; k >= 0; k = i - j) {
            int num = Integer.parseInt(String.valueOf(cardNumber.charAt(k)));
            num = doubleInd ? num * 2 : num;
            num = num > 9 ? num - 9 : num;
            doubleInd = !doubleInd;
            sum += num;
            ++j;
        }

        return sum % 10 == 0;
    }
}
