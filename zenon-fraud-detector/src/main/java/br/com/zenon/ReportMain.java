package br.com.zenon;

import br.com.zenon.TransactionReport.Statistics;

import java.text.NumberFormat;
import java.util.Locale;

public class ReportMain {
    void main() {

        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
        nf.setMinimumFractionDigits(0);
        nf.setMaximumFractionDigits(2);

        var transactionReport = new TransactionReport();
        Statistics statistics= transactionReport.generateReport("data/database.csv");
        IO.println("""
                Total de Linhas: %s
                Total de Fraudes: %s
                Valor total: %s
                """
        .formatted(nf.format(statistics.totalTransactions()), nf.format(statistics.totalFrauds()), nf.format(statistics.totalAmount())));
    }
}
