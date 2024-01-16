package antifraud.database.repositories;

import antifraud.database.entities.TransactionEntity;
import antifraud.utilities.WorldRegion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    @Query("SELECT DISTINCT t.worldRegion FROM TransactionEntity t WHERE t.cardNumber = :cardNumber AND t.date BETWEEN :start AND :end")
    List<WorldRegion> findDistinctWorldRegionsByCardNumberAndDateRange(String cardNumber, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT DISTINCT t.ipAddress FROM TransactionEntity t WHERE t.cardNumber = :cardNumber AND t.date BETWEEN :start AND :end")
    List<String> findDistinctIpAddressesByCardNumberAndDateRange(String cardNumber, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<TransactionEntity> findByCardNumber(String cardNumber);

    List<TransactionEntity> findByIpAddress(String ipAddress);


}
