package br.com.zenon;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FraudAnalyzer {

    private final List<Transaction> transactions;

    FraudAnalyzer(List<Transaction> transactions) {
        Objects.requireNonNull(transactions);
        this.transactions = transactions;

    }

    public long countFrauds() {
        return transactions
                .stream()
                .filter(transaction -> transaction.isFraud())
                .count();
    }


    public List<Transaction> findHighestFrauds(int limit) {
        return fraudStream()
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .limit(limit)
                .toList();
    }

    private Stream<Transaction> fraudStream() {
        return transactions
                .stream()
                .filter(Transaction::isFraud);
    }

    public List<String> findSuspiciousClients(int limit) {
        return fraudStream()
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .map(transaction -> transaction.origin().name())
                .distinct()
                .limit(limit)
                .toList();
    }

    public BigDecimal calculateTotalFraudLoss() {
        return fraudStream()
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                ;
    }

    public Map<TransactionType,Long> countFraudsByType() {
        return fraudStream()
                .collect(Collectors.groupingBy(Transaction::type, Collectors.counting()))
                ;

    }
}
