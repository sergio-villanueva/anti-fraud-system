package antifraud.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StolenPaymentCardDTO {
    private long id;
    private String cardNumber;
}
