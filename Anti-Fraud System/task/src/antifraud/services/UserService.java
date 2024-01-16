package antifraud.services;

import antifraud.database.entities.UserEntity;
import antifraud.database.repositories.UserRepository;
import antifraud.exceptions.AlreadyHasRoleException;
import antifraud.exceptions.BlockAdminException;
import antifraud.exceptions.UserAlreadyExistsException;
import antifraud.models.UserAccessRequest;
import antifraud.models.UserDTO;
import antifraud.models.UserRequest;
import antifraud.models.UserRoleRequest;
import antifraud.utilities.LockToggle;
import antifraud.utilities.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static antifraud.utilities.Role.ADMINISTRATOR;
import static antifraud.utilities.Role.MERCHANT;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    /** Creates a user in the database
     * @param user the request containing user info
     * @exception UserAlreadyExistsException thrown if the user already exists
     */
    public UserDTO createUser(UserRequest user) throws UserAlreadyExistsException {
        user.setUsername(user.getUsername().toLowerCase()); // make username case-insensitive
        if (userRepository.existsByUsername(user.getUsername())) throw new UserAlreadyExistsException("user already exists");

        boolean merchantIndicator = userRepository.existsById(1L);
        Timestamp timestamp = new Timestamp(new Date().getTime());
        UserEntity entity = UserEntity.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .name(user.getName())
                .role(merchantIndicator ? MERCHANT : ADMINISTRATOR)
                .created(timestamp)
                .updated(timestamp)
                .accountLockedInd(LockToggle.fetchByLogicToggle(merchantIndicator))
                .build();

        return toUserDTO(userRepository.save(entity));
    }

    /** Retrieves a list of users sorted in ascending order by id
     * */
    public List<UserDTO> retrieveAllUsers() {
        // list of all users sorted in ascending order by id number
        List<UserEntity> entityList = userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        return entityList.stream().map(this::toUserDTO).collect(Collectors.toList());
    }

    /** Deletes a user in the database
     * @param username the username to search by
     * */
    public void deleteUser(String username) {
        long deleteCount = userRepository.deleteByUsername(username.toLowerCase());
        if (deleteCount == 0) throw new UsernameNotFoundException("delete unsuccessful: username not found");
    }

    /** Updates the access level for a user for non-administrators
     * @param accessRequest the request containing access update info
     * @exception BlockAdminException thrown if updating access for an administrator
     * */
    public UserDTO updateUserAccess(UserAccessRequest accessRequest) {
        Optional<UserEntity> entityOptional = userRepository.findByUsername(accessRequest.getUsername());
        entityOptional.ifPresentOrElse(userEntity -> {
            // if user exists
            if (ADMINISTRATOR.equals(userEntity.getRole())) throw new BlockAdminException();
            // save new lock status in db
            LockToggle toggle = LockToggle.fetchByStringToggleNullable(accessRequest.getOperation());
            userEntity.setAccountLockedInd(toggle);
            userRepository.save(userEntity);
        }, () -> {
            // otherwise
            throw new UsernameNotFoundException("username not found");
        });

        return toUserDTO(entityOptional.get());
    }

    /** Updates the role of a user
     * @param roleRequest the request containing role update info
     * @exception UsernameNotFoundException thrown if user does not exist
     * */
    public UserDTO updateUserRole(UserRoleRequest roleRequest) {

        Optional<UserEntity> optional = userRepository.findByUsername(roleRequest.getUsername());
        optional.ifPresentOrElse(userEntity -> {
           // if user exists
           Role newRole = Role.fetchByStringRoleNullable(roleRequest.getRole());
           if (Objects.equals(newRole, userEntity.getRole())) {throw new AlreadyHasRoleException();}
           userEntity.setRole(newRole);
           userRepository.save(userEntity);
        }, () -> {
            // otherwise
            throw new UsernameNotFoundException("username not found");
        });

        return toUserDTO(optional.get());
    }

    private UserDTO toUserDTO(UserEntity entity) {
        return UserDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .username(entity.getUsername())
                .role(Role.fetchStringRoleNullable(entity.getRole()))
                .accountLockedInd(LockToggle.fetchStringToggleNullable(entity.getAccountLockedInd()))
                .build();
    }

}
