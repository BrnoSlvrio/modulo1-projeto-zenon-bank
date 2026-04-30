package br.com.zenon;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.List;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    void main() {
        var t1 = new Transaction(1, TransactionType.PAYMENT, new BigDecimal("9839.64"),
            new TransactionCustomer("C1231006815", new BigDecimal("170136.0"), new BigDecimal("160296.36")),
            new TransactionCustomer("M1979787155", new BigDecimal("0.0"), new BigDecimal("0.0")),
            false, false);

        var t2 = new Transaction(743, TransactionType.CASH_OUT, new BigDecimal("850002.52"),
                new TransactionCustomer("C1280323807", new BigDecimal("850002.52"), new BigDecimal("0.0")),
                new TransactionCustomer("C873221189", new BigDecimal("6510099.11"), new BigDecimal("7360101.63")),
                true, false);

        System.out.println(t1);
        System.out.println(t2);
        IO.println(t1);
        IO.println("---------------------------------------------------------------------");

        var transactionIngestor = new TransactionIngestor();
        List<Transaction> transactions = transactionIngestor.read("data/database.csv");
        IO.println(transactions.size());

        //transactions.forEach(IO::println);
        transactions.stream().limit(10).forEach(IO::println);


        IO.println("---------------------------------------------------------------------");

        var fraudAnalyzer = new FraudAnalyzer(transactions);
        long fraudCount = fraudAnalyzer.countFrauds();

        IO.println("O número total de fraudes é: " + fraudCount);

        IO.println("---------------------------------------------------------------------");

        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);

        List<Transaction> highestFrauds = fraudAnalyzer.findHighestFrauds(3);
        highestFrauds.stream().map(Transaction::amount)
                .forEach(amount -> IO.println(nf.format(amount)));

        IO.println("---------------------------------------------------------------------");

        List<String> suspiciousClients = fraudAnalyzer.findSuspiciousClients(5);

        IO.println("Top 5 clientes suspeitos: ");
        suspiciousClients.forEach(IO::println);

        IO.println("---------------------------------------------------------------------");

        BigDecimal totalFraudLoss = fraudAnalyzer.calculateTotalFraudLoss();
        IO.println("Prejuízo total: " + nf.format(totalFraudLoss));

        IO.println("---------------------------------------------------------------------");

        Map<TransactionType, Long> fraudCountByType = fraudAnalyzer.countFraudsByType();
        IO.println("Fraudes por tipo:");
        //IO.println(fraudCountByType);
        fraudCountByType.forEach((type, count) -> IO.println("- %s: %d".formatted(type, count)));

        IO.println("---------------------------------------------------------------------");

        TransactionRepository transactionRepository;
        transactionRepository = new TransactionListRepository(transactions);
        String notFoundOriginName = "C12345";
        transactionRepository.findByOriginName(notFoundOriginName)
                .ifPresentOrElse(IO::println, () -> IO.println("Transacao nao encontrada para "+ notFoundOriginName));

        String existingOriginName = "C1868032458";

        long startTime = System.nanoTime();
        transactionRepository.findByOriginName(existingOriginName)
                .ifPresentOrElse(IO::println, () -> IO.println("Transacao nao encontrada para "+ existingOriginName));
        long endTime = System.nanoTime();

        IO.println("Tempo de busca - List (ms): "+(endTime - startTime) / 1_000_000.0);

        transactionRepository = new TransactionMapRepository(transactions);

        startTime = System.nanoTime();
        transactionRepository.findByOriginName(existingOriginName)
                .ifPresentOrElse(IO::println, () -> IO.println("Transacao nao encontrada para "+ existingOriginName));
        endTime = System.nanoTime();

        IO.println("Tempo de busca - Map (ms): "+(endTime - startTime) / 1_000_000.0);
    }
}
