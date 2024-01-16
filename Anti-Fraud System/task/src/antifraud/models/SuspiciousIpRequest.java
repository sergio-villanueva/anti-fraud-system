package antifraud.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SuspiciousIpRequest {
    @JsonProperty("ip")
    private String ipAddress;
}
