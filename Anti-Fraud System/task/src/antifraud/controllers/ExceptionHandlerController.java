package antifraud.controllers;

import antifraud.exceptions.*;
import antifraud.generators.ExceptionalResponseGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @Autowired
    private ExceptionalResponseGenerator exceptionalResponseGenerator;

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<Object> handleIllegalArgumentScenario(IllegalArgumentException ex) {
        return exceptionalResponseGenerator.illegalArgumentFailure(ex);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    private ResponseEntity<Object> handleUserAlreadyExistsScenario(UserAlreadyExistsException ex) {
        return exceptionalResponseGenerator.userAlreadyExistsFailure(ex);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    private ResponseEntity<Object> handleUsernameNotFoundScenario(UsernameNotFoundException ex) {
        return exceptionalResponseGenerator.usernameNotFoundFailure(ex);
    }

    @ExceptionHandler(ValidationException.class)
    private ResponseEntity<Object> handleValidationScenario(ValidationException ex) {
        return exceptionalResponseGenerator.validationFailure(ex);
    }

    @ExceptionHandler(AlreadyHasRoleException.class)
    private ResponseEntity<Object> handleAlreadyHasRoleScenario(AlreadyHasRoleException ex) {
        return exceptionalResponseGenerator.alreadyHasRoleFailure(ex);
    }

    @ExceptionHandler(BlockAdminException.class)
    private ResponseEntity<Object> handleBlockAdminScenario(BlockAdminException ex) {
        return exceptionalResponseGenerator.blockAdminFailure(ex);
    }

    @ExceptionHandler(InvalidIpAddressException.class)
    private ResponseEntity<Object> handleInvalidIpAddressScenario(InvalidIpAddressException ex) {
        return exceptionalResponseGenerator.invalidIpAddressFailure(ex);
    }

    @ExceptionHandler(IpAddressAlreadyExistsException.class)
    private ResponseEntity<Object> handleIpAddressAlreadyExistsScenario(IpAddressAlreadyExistsException ex) {
        return exceptionalResponseGenerator.ipAddressAlreadyExistsFailure(ex);
    }

    @ExceptionHandler(IpAddressDoesNotExistException.class)
    private ResponseEntity<Object> handleIpAddressDoesNotExistScenario(IpAddressDoesNotExistException ex) {
        return exceptionalResponseGenerator.ipAddressDoesNotExistFailure(ex);
    }

    @ExceptionHandler(InvalidCardNumberException.class)
    private ResponseEntity<Object> handleInvalidCardNumberScenario(InvalidCardNumberException ex) {
        return exceptionalResponseGenerator.invalidCardNumberFailure(ex);
    }

    @ExceptionHandler(CardNumberAlreadyExistsException.class)
    private ResponseEntity<Object> handleCardNumberAlreadyExistsScenario(CardNumberAlreadyExistsException ex) {
        return exceptionalResponseGenerator.cardNumberAlreadyExistsFailure(ex);
    }

    @ExceptionHandler(CardNumberDoesNotExistException.class)
    private ResponseEntity<Object> handleCardNumberDoesNotExistScenario(CardNumberDoesNotExistException ex) {
        return exceptionalResponseGenerator.cardNumberDoesNotExistFailure(ex);
    }

    @ExceptionHandler(InvalidRegionException.class)
    private ResponseEntity<Object> handleInvalidRegionScenario(InvalidRegionException ex) {
        return exceptionalResponseGenerator.invalidRegionFailure(ex);
    }

    @ExceptionHandler(InvalidDateFormatException.class)
    private ResponseEntity<Object> handleInvalidDateFormatScenario(InvalidDateFormatException ex) {
        return exceptionalResponseGenerator.invalidDateFormatFailure(ex);
    }

    @ExceptionHandler(InvalidTransactionStateException.class)
    private ResponseEntity<Object> handleInvalidTransactionStateScenario(InvalidTransactionStateException ex) {
        return exceptionalResponseGenerator.invalidTransactionStateFailure(ex);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    private ResponseEntity<Object> handleTransactionNotFoundScenario(TransactionNotFoundException ex) {
        return exceptionalResponseGenerator.transactionNotFoundFailure(ex);
    }

    @ExceptionHandler(FeedbackAlreadyExistsException.class)
    private ResponseEntity<Object> handleFeedbackAlreadyExistsScenario(FeedbackAlreadyExistsException ex) {
        return exceptionalResponseGenerator.feedbackAlreadyExistsFailure(ex);
    }

    @ExceptionHandler(SameResultAndFeedbackException.class)
    private ResponseEntity<Object> handleSameResultAndFeedbackScenario(SameResultAndFeedbackException ex) {
        return exceptionalResponseGenerator.sameResultAndFeedbackFailure(ex);
    }

}
