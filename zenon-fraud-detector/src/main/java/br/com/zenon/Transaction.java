package br.com.zenon;

import java.math.BigDecimal;

/**
 * @param isFraud fraud detector
 */
public record Transaction(int step, TransactionType type, BigDecimal amount, TransactionCustomer origin,
                          TransactionCustomer recipient, boolean isFraud, boolean isFlaggedFraud) {

}
