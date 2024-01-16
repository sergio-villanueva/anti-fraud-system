package antifraud.utilities;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum LockToggle {
    // todo: rename to AccessToggle and set LOCK=false and UNLOCK=true
    LOCK("LOCK", true),
    UNLOCK("UNLOCK", false);

    private final String stringToggle;
    private final boolean logicToggle;

    LockToggle(String stringToggle, boolean logicToggle) {
        this.stringToggle = stringToggle;
        this.logicToggle = logicToggle;
    }

    public static LockToggle fetchByStringToggleNullable(String stringToggle) {
        for (LockToggle lockToggle : values()) {
            if (lockToggle.getStringToggle().equalsIgnoreCase(stringToggle)) return lockToggle;
        }
        return null;
    }

    public static LockToggle fetchByLogicToggle(boolean lockIndicator) {
        return lockIndicator ? LOCK : UNLOCK;
    }

    public static String fetchStringToggleNullable(LockToggle lockToggle) {
        return Objects.isNull(lockToggle) ? null : lockToggle.getStringToggle();
    }

    public static boolean fetchLogicToggleNullable(LockToggle lockToggle) {
        return Objects.nonNull(lockToggle) && lockToggle.isLogicToggle();
    }
}
