package antifraud.exceptions;

public class InvalidIpAddressException extends Exception {
    public InvalidIpAddressException() {
        super("invalid ip address");
    }

    public InvalidIpAddressException(String message) {
        super(message);
    }
}
