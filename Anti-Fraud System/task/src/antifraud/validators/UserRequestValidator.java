package antifraud.validators;

import antifraud.database.repositories.UserRepository;
import antifraud.exceptions.DeleteAdminException;
import antifraud.exceptions.ValidationException;
import antifraud.models.UserAccessRequest;
import antifraud.models.UserRequest;
import antifraud.models.UserRoleRequest;
import antifraud.utilities.LockToggle;
import antifraud.utilities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static antifraud.utilities.Role.ADMINISTRATOR;

@Component
public class UserRequestValidator {
    @Autowired
    private UserRepository userRepository;

    public void validateUserRequest(UserRequest user) throws ValidationException {
        if (Objects.isNull(user)) throw new IllegalArgumentException("invalid user request");
        if (!StringUtils.hasText(user.getName())) throw new ValidationException("Missing required field: name");
        if (!StringUtils.hasText(user.getUsername())) throw new ValidationException("Missing required field: username");
        if (!StringUtils.hasText(user.getPassword())) throw new ValidationException("Missing required field: password");

    }

    public void validateDeleteUser(String username) throws ValidationException {
        if (!StringUtils.hasText(username)) throw new ValidationException("Missing required parameter: username");
        userRepository.findByUsername(username.toLowerCase()).ifPresent(userEntity -> {
            if (ADMINISTRATOR.equals(userEntity.getRole())) throw new DeleteAdminException();
        });
    }

    public void validateUserRoleRequest(UserRoleRequest roleRequest) throws ValidationException {
        if (Objects.isNull(roleRequest)) throw new IllegalArgumentException("invalid user request");
        if (!StringUtils.hasText(roleRequest.getUsername())) throw new ValidationException("Missing required field: username");
        Role newRole = Role.fetchByStringRoleNullable(roleRequest.getRole());
        Set<Role> roleSet = Arrays.stream(Role.values()).filter(role -> !ADMINISTRATOR.equals(role)).collect(Collectors.toSet());
        if (Objects.isNull(newRole) || !roleSet.contains(newRole)) throw new IllegalArgumentException("Invalid role");

    }

    public void validateUserAccessRequest(UserAccessRequest accessRequest) {
        LockToggle lockToggle = LockToggle.fetchByStringToggleNullable(accessRequest.getOperation());
        if (Objects.isNull(lockToggle)) throw new IllegalArgumentException("Invalid access operation");
    }

}
