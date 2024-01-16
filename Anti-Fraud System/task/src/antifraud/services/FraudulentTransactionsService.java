package antifraud.services;

import antifraud.models.Transaction;
import antifraud.utilities.TransactionState;

public interface FraudulentTransactionsService {
    /** Verify the transaction
     * @param transaction the transaction to be verified for fraud
     * @return state after a transaction has been verified
     * */
    TransactionState verify(Transaction transaction);
}
