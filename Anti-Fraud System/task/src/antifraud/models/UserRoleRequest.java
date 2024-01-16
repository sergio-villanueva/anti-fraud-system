package antifraud.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserRoleRequest {
    @JsonProperty("username")
    private String username;
    @JsonProperty("role")
    private String role;
}
