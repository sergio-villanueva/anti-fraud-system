package antifraud.database.repositories;

import antifraud.database.entities.UserEntity;
import antifraud.database.entities.UserPKEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UserPKEntity> {
    boolean existsById(Long id);
    boolean existsByUsername(String username);
    Optional<UserEntity> findByUsername(String username);
    @Transactional
    long deleteByUsername(String username);

}
