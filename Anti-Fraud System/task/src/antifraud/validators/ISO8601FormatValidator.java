package antifraud.validators;

import antifraud.exceptions.InvalidDateFormatException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component("iso8601FormatValidator")
public class ISO8601FormatValidator implements DateFormatValidator{
    /**
     * Validate if the given date is compliant with the ISO 8601 date format
     *
     * @param date the given data
     * @throws InvalidDateFormatException thrown if the date violates the date format
     */
    @Override
    public void validate(String date) throws InvalidDateFormatException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        try {
            LocalDateTime.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException("date violates the following format: yyyy-MM-dd'T'HH:mm:ss");
        }

    }
}
