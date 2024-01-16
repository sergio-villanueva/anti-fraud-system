package antifraud.controllers;

import antifraud.exceptions.*;
import antifraud.generators.AntiFraudResponseGenerator;
import antifraud.models.*;
import antifraud.services.FraudulentTransactionsService;
import antifraud.services.StolenPaymentCardService;
import antifraud.services.SuspiciousIpService;
import antifraud.services.TransactionService;
import antifraud.utilities.TransactionState;
import antifraud.validators.CardNumberValidator;
import antifraud.validators.IpAddressValidator;
import antifraud.validators.TransactionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/antifraud")
public class AntiFraudController {

    @Autowired
    private FraudulentTransactionsService fraudulentTransactionsService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private SuspiciousIpService suspiciousIpService;

    @Autowired @Qualifier("ipv4AddressValidator")
    private IpAddressValidator ipAddressValidator;

    @Autowired
    private StolenPaymentCardService stolenPaymentCardService;

    @Autowired @Qualifier("luhnAlgorithm")
    private CardNumberValidator cardNumberValidator;

    @Autowired
    private TransactionValidator transactionValidator;

    @Autowired
    private AntiFraudResponseGenerator antiFraudResponseGenerator;

    private final Logger logger = LoggerFactory.getLogger(AntiFraudController.class);

    @PostMapping("/transaction")
    public ResponseEntity<Object> verifyTransaction(@RequestBody Transaction transaction) throws InvalidCardNumberException, InvalidDateFormatException, InvalidRegionException, InvalidIpAddressException {
        logger.info("start validate transaction journey");
        transactionValidator.validateTransaction(transaction);
        logger.info("field validation successful");
        TransactionState state = fraudulentTransactionsService.verify(transaction);
        logger.info("transaction verified");
        transactionService.createTransaction(transaction, state);
        logger.info("transaction created successfully");
        return antiFraudResponseGenerator.nonFraudalantTransactionSuccess(state);
    }

    @PutMapping("/transaction")
    public ResponseEntity<Object> updateTransactionFeedback(@RequestBody TransactionFeedback transactionFeedback) throws InvalidTransactionStateException, FeedbackAlreadyExistsException, SameResultAndFeedbackException {
        logger.info("start add transaction feedback journey");
        transactionValidator.validateTransactionFeedback(transactionFeedback);
        logger.info("field validation successful");
        TransactionDTO dto = transactionService.updateTransactionFeedback(transactionFeedback);
        logger.info("transaction feedback updated successfully");
        return antiFraudResponseGenerator.updateTransactionFeedbackSuccess(dto);
    }

    @GetMapping("/history")
    public ResponseEntity<Object> retrieveTransactionHistory() {
        logger.info("start retrieve transaction history journey");
        List<TransactionDTO> dtoList = transactionService.retrieveTransactionHistory();
        logger.info("transaction history successfully retrieved");
        logger.info("dto list total db count: " + dtoList.size());
        return antiFraudResponseGenerator.retrieveTransactionHistorySuccess(dtoList);
    }

    @GetMapping("/history/{number}")
    public ResponseEntity<Object> retrieveTransactionHistory(@PathVariable("number") String cardNumber) throws InvalidCardNumberException {
        logger.info("start retrieve transaction history journey");
        cardNumberValidator.validate(cardNumber);
        logger.info("card number validation successful");
        List<TransactionDTO> dtoList = transactionService.retrieveTransactionHistory(cardNumber);
        logger.info("transaction history successfully retrieved");
        return antiFraudResponseGenerator.retrieveTransactionHistorySuccess(dtoList);
    }

    @PostMapping("/suspicious-ip")
    public ResponseEntity<Object> addSuspiciousIpAddress(@RequestBody SuspiciousIpRequest suspiciousIpRequest) throws InvalidIpAddressException, IpAddressAlreadyExistsException {
        logger.info("start add suspicious ip address journey");
        ipAddressValidator.validate(suspiciousIpRequest.getIpAddress());
        logger.info("ip address validation successful");
        SuspiciousIpAddressDTO dto = suspiciousIpService.addSuspiciousIpAddress(suspiciousIpRequest);
        logger.info("suspicious ip address was added in database successfully");
        return antiFraudResponseGenerator.addSuspiciousIpAddressSuccess(dto);
    }

    @GetMapping("/suspicious-ip")
    public ResponseEntity<Object> retrieveAllSuspiciousIpAddresses() {
        logger.info("start retrieve all suspicious ids journey");
        List<SuspiciousIpAddressDTO> suspiciousIpAddressDTOList = suspiciousIpService.retrieveAllSuspiciousIpAddresses();
        logger.info("retrieved all suspicious ip addresses successfully");
        return antiFraudResponseGenerator.retrieveAllSuspiciousIpAddressesSuccess(suspiciousIpAddressDTOList);
    }

    @DeleteMapping("/suspicious-ip/{ip}")
    public ResponseEntity<Object> deleteSuspiciousIpAddress(@PathVariable("ip") String ipAddress) throws InvalidIpAddressException, IpAddressDoesNotExistException {
        logger.info("start delete suspicious ip address journey");
        ipAddressValidator.validate(ipAddress);
        logger.info("ip address validation successful");
        suspiciousIpService.deleteSuspiciousIpAddress(ipAddress);
        logger.info("suspicious ip address was deleted in database successfully");
        return antiFraudResponseGenerator.deleteSuspiciousIpAddressSuccess(ipAddress);
    }

    @PostMapping("/stolencard")
    public ResponseEntity<Object> addStolenPaymentCard(@RequestBody StolenPaymentCardRequest stolenPaymentCardRequest) throws InvalidCardNumberException, CardNumberAlreadyExistsException {
        logger.info("start add stolen payment card journey");
        cardNumberValidator.validate(stolenPaymentCardRequest.getCardNumber());
        logger.info("card number validation successful");
        StolenPaymentCardDTO dto = stolenPaymentCardService.addStolenPaymentCard(stolenPaymentCardRequest);
        logger.info("stolen card number was added in database successfully");
        return antiFraudResponseGenerator.addStolenPaymentCardSuccess(dto);
    }

    @GetMapping("/stolencard")
    public ResponseEntity<Object> retrieveAllStolenPaymentCards() {
        logger.info("start retrieve all stolen payment cards journey");
        List<StolenPaymentCardDTO> stolenPaymentCardDTOList = stolenPaymentCardService.retrieveAllStolenPaymentCards();
        logger.info("retrieved all stolen card numbers successfully");
        return antiFraudResponseGenerator.retrieveAllStolenPaymentCardsSuccess(stolenPaymentCardDTOList);
    }

    @DeleteMapping("/stolencard/{number}")
    public ResponseEntity<Object> deleteStolenPaymentCard(@PathVariable("number") String cardNumber) throws InvalidCardNumberException, CardNumberDoesNotExistException {
        logger.info("start delete stolen payment card journey");
        cardNumberValidator.validate(cardNumber);
        logger.info("card number validation successful");
        stolenPaymentCardService.deleteStolenPaymentCard(cardNumber);
        logger.info("stolen card number was deleted in database successfully");
        return antiFraudResponseGenerator.deleteStolenPaymentCardSuccess(cardNumber);
    }

}
