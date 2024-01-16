package antifraud.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransactionFeedback {
    @JsonProperty("transactionId")
    private long transactionId;

    @JsonProperty("feedback")
    private String feedback;
}
