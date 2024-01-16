package antifraud.exceptions;

public class CardNumberDoesNotExistException extends RuntimeException {
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public CardNumberDoesNotExistException() {
        super("card number does not exist");
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public CardNumberDoesNotExistException(String message) {
        super(message);
    }
}
