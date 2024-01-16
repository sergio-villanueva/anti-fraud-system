package antifraud.exceptions;

public class DeleteAdminException extends RuntimeException {
    public DeleteAdminException() {
        super("cannot delete user that is admin");
    }

    public DeleteAdminException(String message) {
        super(message);
    }
}
