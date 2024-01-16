package antifraud.exceptions;

public class BlockAdminException extends RuntimeException {
    public BlockAdminException() {
        super("cannot block access to an administrator");
    }
}
