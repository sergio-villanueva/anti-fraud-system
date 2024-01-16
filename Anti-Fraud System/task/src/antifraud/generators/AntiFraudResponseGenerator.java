package antifraud.generators;

import antifraud.models.StolenPaymentCardDTO;
import antifraud.models.SuspiciousIpAddressDTO;
import antifraud.models.TransactionDTO;
import antifraud.utilities.TransactionState;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Component
public class AntiFraudResponseGenerator {

    public ResponseEntity<Object> nonFraudalantTransactionSuccess(TransactionState state) {
        NonFraudalantTransactionResponse body = toNonFraudalantTransactionResponse(state);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public ResponseEntity<Object> retrieveTransactionHistorySuccess(List<TransactionDTO> dtoList) {
        List<TransactionResponse> body = dtoList.stream().map(this::toTransactionResponse).toList();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public ResponseEntity<Object> updateTransactionFeedbackSuccess(TransactionDTO dto) {
        TransactionResponse body = toTransactionResponse(dto);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public ResponseEntity<Object> addSuspiciousIpAddressSuccess(SuspiciousIpAddressDTO dto) {
        SuspiciousIpAddressResponse body = toSuspiciousIpAddressResponse(dto);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public ResponseEntity<Object> retrieveAllSuspiciousIpAddressesSuccess(List<SuspiciousIpAddressDTO> suspiciousIpAddressDTOList) {
        List<SuspiciousIpAddressResponse> body = suspiciousIpAddressDTOList.stream().map(this::toSuspiciousIpAddressResponse).toList();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteSuspiciousIpAddressSuccess(String ipAddress) {
        String status = "IP " + ipAddress + " successfully removed!";
        GenericDeleteResponse body = GenericDeleteResponse.builder()
                .status(status)
                .build();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public ResponseEntity<Object> addStolenPaymentCardSuccess(StolenPaymentCardDTO dto) {
        StolenPaymentCardResponse body = toStolenPaymentCardResponse(dto);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public ResponseEntity<Object> retrieveAllStolenPaymentCardsSuccess(List<StolenPaymentCardDTO> stolenPaymentCardDTOList) {
        List<StolenPaymentCardResponse> body = stolenPaymentCardDTOList.stream().map(this::toStolenPaymentCardResponse).toList();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteStolenPaymentCardSuccess(String cardNumber) {
        String status = "Card " + cardNumber + " successfully removed!";
        GenericDeleteResponse body = GenericDeleteResponse.builder()
                .status(status)
                .build();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    private NonFraudalantTransactionResponse toNonFraudalantTransactionResponse(TransactionState state) {
        StringBuilder reasonBuilder = new StringBuilder(state.getReasons().get(0));
        state.getReasons().stream().skip(1).forEach((reason) -> {
            reasonBuilder.append(", ").append(reason);
        });
        return NonFraudalantTransactionResponse.builder()
                .result(state.name())
                .info(reasonBuilder.toString())
                .build();
    }

    private TransactionResponse toTransactionResponse(TransactionDTO dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return TransactionResponse.builder()
                .transactionId(dto.getTransactionId())
                .amount(dto.getAmount())
                .ip(dto.getIpAddress())
                .number(dto.getCardNumber())
                .region(dto.getWorldRegion().getStringRegion())
                .date(dto.getDate().format(formatter))
                .result(dto.getResult().name())
                .feedback(Objects.nonNull(dto.getFeedback()) ? dto.getFeedback().name() : "")
                .build();
    }

    private SuspiciousIpAddressResponse toSuspiciousIpAddressResponse(SuspiciousIpAddressDTO dto) {
        return SuspiciousIpAddressResponse.builder()
                .id(dto.getId())
                .ip(dto.getIpAddress())
                .build();
    }

    private StolenPaymentCardResponse toStolenPaymentCardResponse(StolenPaymentCardDTO dto) {
        return StolenPaymentCardResponse.builder()
                .id(dto.getId())
                .number(dto.getCardNumber())
                .build();
    }

    @Builder
    @Data
    private static class NonFraudalantTransactionResponse {
        private String result;
        private String info;
    }

    @Builder
    @Data
    private static class TransactionResponse {
        private Long transactionId;
        private Long amount;
        private String ip;
        private String number;
        private String region;
        private String date;
        private String result;
        private String feedback;
    }

    @Builder
    @Data
    private static class SuspiciousIpAddressResponse {
        private long id;
        private String ip;
    }

    @Builder
    @Data
    private static class GenericDeleteResponse {
        private String status;
    }

    @Data
    @Builder
    private static class StolenPaymentCardResponse {
        private long id;
        private String number;
    }
}
