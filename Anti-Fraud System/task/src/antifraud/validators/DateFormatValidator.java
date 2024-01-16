package antifraud.validators;

import antifraud.exceptions.InvalidDateFormatException;

public interface DateFormatValidator {
    /** Validate if the given date is compliant with the date format
     * @param date the given data
     * @exception InvalidDateFormatException thrown if the date violates the date format
     * */
    void validate(String date) throws InvalidDateFormatException;
}
