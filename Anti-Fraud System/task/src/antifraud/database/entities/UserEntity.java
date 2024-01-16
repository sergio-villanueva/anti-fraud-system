package antifraud.database.entities;

import antifraud.utilities.LockToggle;
import antifraud.utilities.LockToggleConverter;
import antifraud.utilities.Role;
import antifraud.utilities.RoleConverter;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@IdClass(UserPKEntity.class)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Id
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "name")
    private String name;
    @Column(name = "role")
    @Convert(converter = RoleConverter.class)
    private Role role;
    @Column(name = "created_timestamp")
    private Timestamp created;
    @Column(name = "updated_timestamp")
    private Timestamp updated;
    @Column(name = "account_locked_ind")
    @Convert(converter = LockToggleConverter.class)
    private LockToggle accountLockedInd;
}
