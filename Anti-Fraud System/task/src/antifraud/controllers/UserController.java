package antifraud.controllers;

import antifraud.exceptions.UserAlreadyExistsException;
import antifraud.exceptions.ValidationException;
import antifraud.generators.UserResponseGenerator;
import antifraud.models.UserAccessRequest;
import antifraud.models.UserDTO;
import antifraud.models.UserRequest;
import antifraud.models.UserRoleRequest;
import antifraud.services.UserService;
import antifraud.validators.UserRequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRequestValidator validator;
    @Autowired
    private UserResponseGenerator userResponseGenerator;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    // registration entry point
    @PostMapping("/user")
    public ResponseEntity<Object> createUser(@RequestBody UserRequest user) throws UserAlreadyExistsException, ValidationException {
        logger.info("start create user journey");
        validator.validateUserRequest(user);
        logger.info("user request successfully validated");
        UserDTO userDTO = userService.createUser(user);
        logger.info("user created successfully");
        return userResponseGenerator.registrationSuccess(userDTO);
    }

    // fetches all registered users to anti-fraud system
    @GetMapping("/list")
    public ResponseEntity<Object> retrieveAllUsers() {
        logger.info("start get all users journey");
        List<UserDTO> userDTOList = userService.retrieveAllUsers();
        logger.info("all users retrieved successfully");
        return userResponseGenerator.allUsersSuccess(userDTOList);
    }

    // deletes a user
    @DeleteMapping("/user/{username}")
    public ResponseEntity<Object> deleteUser(@PathVariable("username") String username) throws ValidationException {
        logger.info("start delete user journey");
        validator.validateDeleteUser(username);
        logger.info("username successfully validated");
        userService.deleteUser(username);
        logger.info("user deleted successfully");
        return userResponseGenerator.deleteSuccess(username);
    }

    // allows for locking/unlocking a user
    @PutMapping("/access")
    public ResponseEntity<Object> updateUserAccess(@RequestBody UserAccessRequest accessRequest) {
        logger.info("start toggle user access journey");
        validator.validateUserAccessRequest(accessRequest);
        logger.info("user access request successfully validated");
        UserDTO userDTO = userService.updateUserAccess(accessRequest);
        logger.info("user access modification successful");
        return userResponseGenerator.updateUserAccessSuccess(userDTO);
    }

    // updates the role for a user
    @PutMapping("/role")
    public ResponseEntity<Object> updateUserRole(@RequestBody UserRoleRequest roleRequest) throws ValidationException {
        logger.info("start update user role journey");
        validator.validateUserRoleRequest(roleRequest);
        logger.info("user role request successfully validated");
        UserDTO userDTO = userService.updateUserRole(roleRequest);
        logger.info("user role modification successful");
        return userResponseGenerator.updateUserRoleSuccess(userDTO);
    }
}
