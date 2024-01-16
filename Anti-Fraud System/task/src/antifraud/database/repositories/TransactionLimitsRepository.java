package antifraud.database.repositories;

import antifraud.database.entities.TransactionLimitsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionLimitsRepository extends JpaRepository<TransactionLimitsEntity, Long> {
    Optional<TransactionLimitsEntity> findByCardNumber(String cardNumber);

}