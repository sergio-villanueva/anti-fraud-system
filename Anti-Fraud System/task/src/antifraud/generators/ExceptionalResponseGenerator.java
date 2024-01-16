package antifraud.generators;

import antifraud.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ExceptionalResponseGenerator {

    public ResponseEntity<Object> illegalArgumentFailure(IllegalArgumentException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status: ", HttpStatus.BAD_REQUEST.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("exception: ", ex.getClass());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Object> userAlreadyExistsFailure(UserAlreadyExistsException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status: ", HttpStatus.CONFLICT.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("exception: ", ex.getClass());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    public ResponseEntity<Object> usernameNotFoundFailure(UsernameNotFoundException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status: ", HttpStatus.NOT_FOUND.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("exception: ", ex.getClass());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Object> validationFailure(ValidationException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status: ", HttpStatus.BAD_REQUEST.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("exception: ", ex.getClass());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Object> alreadyHasRoleFailure(AlreadyHasRoleException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status: ", HttpStatus.CONFLICT.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("exception: ", ex.getClass());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    public ResponseEntity<Object> blockAdminFailure(BlockAdminException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status: ", HttpStatus.BAD_REQUEST.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("exception: ", ex.getClass());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Object> invalidIpAddressFailure(InvalidIpAddressException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status: ", HttpStatus.BAD_REQUEST.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("exception: ", ex.getClass());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Object> ipAddressAlreadyExistsFailure(IpAddressAlreadyExistsException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status: ", HttpStatus.CONFLICT.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("exception: ", ex.getClass());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    public ResponseEntity<Object> ipAddressDoesNotExistFailure(IpAddressDoesNotExistException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status: ", HttpStatus.NOT_FOUND.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("exception: ", ex.getClass());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Object> invalidCardNumberFailure(InvalidCardNumberException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status: ", HttpStatus.BAD_REQUEST.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("exception: ", ex.getClass());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Object> cardNumberAlreadyExistsFailure(CardNumberAlreadyExistsException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status: ", HttpStatus.CONFLICT.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("exception: ", ex.getClass());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    public ResponseEntity<Object> cardNumberDoesNotExistFailure(CardNumberDoesNotExistException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status: ", HttpStatus.NOT_FOUND.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("exception: ", ex.getClass());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Object> invalidRegionFailure(InvalidRegionException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status: ", HttpStatus.BAD_REQUEST.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("exception: ", ex.getClass());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Object> invalidDateFormatFailure(InvalidDateFormatException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status: ", HttpStatus.BAD_REQUEST.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("exception: ", ex.getClass());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Object> invalidTransactionStateFailure(InvalidTransactionStateException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status: ", HttpStatus.BAD_REQUEST.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("exception: ", ex.getClass());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Object> transactionNotFoundFailure(TransactionNotFoundException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status: ", HttpStatus.NOT_FOUND.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("exception: ", ex.getClass());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Object> feedbackAlreadyExistsFailure(FeedbackAlreadyExistsException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status: ", HttpStatus.CONFLICT.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("exception: ", ex.getClass());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    public ResponseEntity<Object> sameResultAndFeedbackFailure(SameResultAndFeedbackException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status: ", HttpStatus.UNPROCESSABLE_ENTITY.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("exception: ", ex.getClass());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
