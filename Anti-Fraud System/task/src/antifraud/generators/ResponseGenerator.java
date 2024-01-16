package antifraud.generators;

import antifraud.exceptions.AlreadyHasRoleException;
import antifraud.exceptions.BlockAdminException;
import antifraud.exceptions.UserAlreadyExistsException;
import antifraud.exceptions.ValidationException;
import antifraud.models.UserDTO;
import antifraud.utilities.LockToggle;
import antifraud.utilities.TransactionState;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static antifraud.utilities.LockToggle.LOCK;

/**
 * Generates custom responses for each web endpoint
 */
@Component
@Deprecated
public class ResponseGenerator {
    /**
     * @param dto used to extract data for response
     * @return ResponseEntity
     */
    //migrated
    public ResponseEntity<Object> registrationSuccess(UserDTO dto) {
        // todo: return the user role
        UserRegistrationResponse body = toUserRegistrationResponse(dto);
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    //migrated
    public ResponseEntity<Object> allUsersSuccess(List<UserDTO> dtoList) {
        // todo: return the user role
        List<UserRegistrationResponse> body = dtoList.stream()
                .map(this::toUserRegistrationResponse).collect(Collectors.toList());
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    //migrated
    public ResponseEntity<Object> deleteSuccess(String username) {
        UserDeletionResponse body = UserDeletionResponse.builder()
                .username(username)
                .status("Deleted successfully!")
                .build();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    //migrated
    public ResponseEntity<Object> nonFraudalantTransactionSuccess(TransactionState state) {
        NonFraudalantTransactionResponse body = NonFraudalantTransactionResponse.builder().result(state.name()).build();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    //migrated
    public ResponseEntity<Object> updateUserRoleSuccess(UserDTO userDTO) {
        UserRegistrationResponse body = toUserRegistrationResponse(userDTO);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    //migrated
    public ResponseEntity<Object> updateUserAccessSuccess(UserDTO userDTO) {
        boolean lockedInd = LOCK.equals(LockToggle.fetchByStringToggleNullable(userDTO.getAccountLockedInd()));
        String statusMessage = "User " + userDTO.getUsername();
        statusMessage = lockedInd ? statusMessage + " locked!" : statusMessage + " unlocked!";

        UserAccessResponse body = UserAccessResponse.builder()
                .status(statusMessage)
                .build();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public ResponseEntity<Object> illegalArgumentFailure(IllegalArgumentException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status: ", HttpStatus.BAD_REQUEST.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("exception: ", ex.getClass());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Object> userAlreadyExistsFailure(UserAlreadyExistsException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status: ", HttpStatus.CONFLICT.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("exception: ", ex.getClass());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    public ResponseEntity<Object> usernameNotFoundFailure(UsernameNotFoundException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status: ", HttpStatus.NOT_FOUND.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("exception: ", ex.getClass());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Object> validationFailure(ValidationException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status: ", HttpStatus.BAD_REQUEST.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("exception: ", ex.getClass());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Object> alreadyHasRoleFailure(AlreadyHasRoleException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status: ", HttpStatus.CONFLICT.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("exception: ", ex.getClass());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    public ResponseEntity<Object> blockAdminFailure(BlockAdminException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status: ", HttpStatus.BAD_REQUEST.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("exception: ", ex.getClass());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    private UserRegistrationResponse toUserRegistrationResponse(UserDTO dto) {
        return UserRegistrationResponse.builder()
                .id(dto.getId())
                .name(dto.getName())
                .username(dto.getUsername())
                .role(dto.getRole())
                .build();
    }


    @Builder
    @Data
    private static class UserRegistrationResponse {
        private Long id;
        private String name;
        private String username;
        private String role;
    }

    @Builder
    @Data
    private static class UserDeletionResponse {
        private String username;
        private String status;
    }

    @Builder
    @Data
    private static class UserAccessResponse {
        private String status;
    }

    @Builder
    @Data
    private static class NonFraudalantTransactionResponse {
        private String result;
    }
}
