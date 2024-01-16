package antifraud.database.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class UserPKEntity implements Serializable {
    private Long id;
    private String username;
}
