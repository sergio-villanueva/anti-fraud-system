package antifraud.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StolenPaymentCardRequest {
    @JsonProperty("number")
    private String cardNumber;
}
