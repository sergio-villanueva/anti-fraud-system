package antifraud.database.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction_limits")
public class TransactionLimitsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "max_allowed_limit", nullable = false)
    private Long maxAllowedLimit;

    @Column(name = "max_manual_limit", nullable = false)
    private Long maxManualLimit;

    @Builder.Default
    @OneToMany(mappedBy = "transactionLimitsEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<TransactionEntity> transactionEntities = new LinkedHashSet<>();

}
