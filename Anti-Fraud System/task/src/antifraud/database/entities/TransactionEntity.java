package antifraud.database.entities;

import antifraud.utilities.WorldRegion;
import antifraud.utilities.WorldRegionConverter;
import antifraud.utilities.TransactionState;
import antifraud.utilities.TransactionStateConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "region", nullable = false)
    @Convert(converter = WorldRegionConverter.class)
    private WorldRegion worldRegion;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "result", nullable = false)
    @Convert(converter = TransactionStateConverter.class)
    private TransactionState result;

    @Column(name = "feedback")
    @Convert(converter = TransactionStateConverter.class)
    private TransactionState feedback;

    @ManyToOne
    @JoinColumn(name = "stolen_id", referencedColumnName = "id")
    private StolenPaymentCardEntity stolenPaymentCardEntity;

    @ManyToOne
    @JoinColumn(name = "suspicious_id", referencedColumnName = "id")
    private SuspiciousIpAddressEntity suspiciousIpAddressEntity;

    @Builder.Default
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "transaction_info", joinColumns = @JoinColumn(name = "transaction_id", referencedColumnName = "id"))
    private List<String> info = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "transaction_limits_id", referencedColumnName = "id")
    private TransactionLimitsEntity transactionLimitsEntity;
    // above generated table contains columns for transaction_id and info

}
