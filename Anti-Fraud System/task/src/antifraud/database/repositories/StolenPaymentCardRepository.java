package antifraud.database.repositories;

import antifraud.database.entities.StolenPaymentCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface StolenPaymentCardRepository extends JpaRepository<StolenPaymentCardEntity, Long> {
    boolean existsByCardNumber(String cardNumber);

    @Transactional
    long deleteByCardNumber(String cardNumber);

    Optional<StolenPaymentCardEntity> findByCardNumber(String cardNumber);

}
