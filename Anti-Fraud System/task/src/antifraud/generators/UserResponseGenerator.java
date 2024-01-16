package antifraud.generators;

import antifraud.models.UserDTO;
import antifraud.utilities.LockToggle;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static antifraud.utilities.LockToggle.LOCK;

@Component
public class UserResponseGenerator {


    public ResponseEntity<Object> registrationSuccess(UserDTO dto) {
        // todo: return the user role
        UserRegistrationResponse body = toUserRegistrationResponse(dto);
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> allUsersSuccess(List<UserDTO> dtoList) {
        // todo: return the user role
        List<UserRegistrationResponse> body = dtoList.stream()
                .map(this::toUserRegistrationResponse).collect(Collectors.toList());
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteSuccess(String username) {
        UserDeletionResponse body = UserDeletionResponse.builder()
                .username(username)
                .status("Deleted successfully!")
                .build();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public ResponseEntity<Object> updateUserRoleSuccess(UserDTO userDTO) {
        UserRegistrationResponse body = toUserRegistrationResponse(userDTO);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public ResponseEntity<Object> updateUserAccessSuccess(UserDTO userDTO) {
        boolean lockedInd = LOCK.equals(LockToggle.fetchByStringToggleNullable(userDTO.getAccountLockedInd()));
        String statusMessage = "User " + userDTO.getUsername();
        statusMessage = lockedInd ? statusMessage + " locked!" : statusMessage + " unlocked!";

        UserAccessResponse body = UserAccessResponse.builder()
                .status(statusMessage)
                .build();
        return new ResponseEntity<>(body, HttpStatus.OK);
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
}
