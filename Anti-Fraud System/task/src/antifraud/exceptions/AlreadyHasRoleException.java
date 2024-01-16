package antifraud.exceptions;

public class AlreadyHasRoleException extends RuntimeException {
    public AlreadyHasRoleException() {
        super("role is already assigned");
    }

    public AlreadyHasRoleException(String message) {
        super(message);
    }
}
