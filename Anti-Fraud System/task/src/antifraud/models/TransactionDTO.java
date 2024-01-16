package antifraud.models;

import antifraud.utilities.TransactionState;
import antifraud.utilities.WorldRegion;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TransactionDTO {
    private Long transactionId;
    private Long amount;
    private String ipAddress;
    private String cardNumber;
    private WorldRegion worldRegion;
    private LocalDateTime date;
    private TransactionState result;
    private TransactionState feedback;
}
