package antifraud.services;

import org.springframework.stereotype.Service;

@Service
public class TransactionLimitServiceImpl implements TransactionLimitService {
    /**
     * Calculates the increase of a transaction limit
     *
     * @param amount       the transaction amount
     * @param currentLimit the current transaction limit used for fraud verification
     * @return the new limit for fraud verification
     */
    @Override
    public long increaseLimit(long amount, long currentLimit) {
        return (long) Math.ceil((0.8 * currentLimit) + (0.2 * amount));
    }

    /**
     * Calculates the decrease of a transaction limit
     *
     * @param amount       the transaction amount
     * @param currentLimit the current transaction limit used for fraud verification
     * @return the new limit for fraud verification
     */
    @Override
    public long decreaseLimit(long amount, long currentLimit) {
        return (long) Math.ceil((0.8 * currentLimit) - (0.2 * amount));
    }
}
