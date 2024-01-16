package antifraud.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String username;
    private String role;
    private String accountLockedInd;
}
