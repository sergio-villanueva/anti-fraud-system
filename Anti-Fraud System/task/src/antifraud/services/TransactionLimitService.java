package antifraud.services;

public interface TransactionLimitService {
    /** Calculates the increase of a transaction limit
     * @param amount the transaction amount
     * @param currentLimit the current transaction limit used for fraud verification
     * @return the new limit for fraud verification
     * */
    long increaseLimit(long amount, long currentLimit);
    /**
     * Calculates the decrease of a transaction limit
     *
     * @param amount       the transaction amount
     * @param currentLimit the current transaction limit used for fraud verification
     * @return the new limit for fraud verification
     */
    long decreaseLimit(long amount, long currentLimit);
}
