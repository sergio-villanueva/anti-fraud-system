package antifraud.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserAccessRequest {
    @JsonProperty("username")
    private String username;
    @JsonProperty("operation")
    private String operation;
}
