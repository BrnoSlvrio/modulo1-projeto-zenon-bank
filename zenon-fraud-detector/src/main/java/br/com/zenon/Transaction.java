package br.com.zenon;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @param isFraud fraud detector
 */
public record Transaction(int step, TransactionType type, BigDecimal amount, TransactionCustomer origin,
                          TransactionCustomer recipient, boolean isFraud, boolean isFlaggedFraud) {

    public Transaction {
        Objects.requireNonNull(type);
        Objects.requireNonNull(amount);
        Objects.requireNonNull(origin);
        Objects.requireNonNull(recipient);

        if(step <= 0) throw new IllegalArgumentException("O valor de step não pode ser 0 nem negativo. Step atual: "+step);
        if(amount.signum() < 0) throw new IllegalArgumentException("O valor de amount não pode ser 0 nem negativo. Amount atual: "+amount);
    }

}
