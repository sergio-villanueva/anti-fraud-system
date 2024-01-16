package antifraud.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Transaction {
    @JsonProperty("amount")
    private long amount;
    @JsonProperty("ip")
    private String ipAddress;
    @JsonProperty("number")
    private String cardNumber;
    @JsonProperty("region")
    private String worldRegion;
    @JsonProperty("date")
    private String date;
}
