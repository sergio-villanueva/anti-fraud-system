package antifraud.database.repositories;

import antifraud.database.entities.SuspiciousIpAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SuspiciousIpAddressRepository extends JpaRepository<SuspiciousIpAddressEntity, Long> {
    boolean existsByIpAddress(String ipAddress);

    @Transactional
    long deleteByIpAddress(String ipAddress);

    Optional<SuspiciousIpAddressEntity> findByIpAddress(String ipAddress);


}
