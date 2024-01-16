package antifraud.utilities;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum Role {
    MERCHANT("ROLE_MERCHANT", "MERCHANT"),
    ADMINISTRATOR("ROLE_ADMINISTRATOR", "ADMINISTRATOR"),
    SUPPORT("ROLE_SUPPORT", "SUPPORT");

    private final String authority;
    private final String role;

    Role(String value, String role) {
        this.authority = value;
        this.role = role;
    }

    public static Role fetchByStringRoleNullable(String stringRole) {
        for (Role role : values()) {
            if (role.getRole().equalsIgnoreCase(stringRole)) return role;
        }
        return null;
    }

    public static String fetchStringRoleNullable(Role role) {
        for (Role r : values()) {
            if (Objects.equals(r,role)) return r.getRole();
        }
        return null;
    }

    public static String fetchStringAuthorityNullable(Role role) {
        for (Role r : values()) {
            if (Objects.equals(r,role)) return r.getAuthority();
        }
        return null;
    }
}
