package antifraud.utilities;

import java.util.ArrayList;
import java.util.List;

public enum TransactionState {
    ALLOWED, MANUAL_PROCESSING, PROHIBITED;

    private List<String> reasons = new ArrayList<>();

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }

    public static TransactionState getNullableTransactionStateByString(String stringState) {
        TransactionState[] states = values();
        for (TransactionState state : states) {
            if (state.name().equals(stringState)) return state;
        }

        return null;
    }
}
