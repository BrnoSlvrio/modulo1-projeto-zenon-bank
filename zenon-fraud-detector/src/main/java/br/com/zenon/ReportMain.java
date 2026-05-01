package br.com.zenon;

import br.com.zenon.TransactionReport.Statistics;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReportMain {
    void main(String[] args) {
        String language = (args.length > 0 ? args[0]: "pt");
        var locale = Locale.of(language);

        var integerFormatter = NumberFormat.getNumberInstance(locale);
        var currencyFormatter = DecimalFormat.getCurrencyInstance(locale);
        currencyFormatter.setCurrency(Currency.getInstance("USD"));

        var resourceBundle = ResourceBundle.getBundle("report", locale);

        var transactionReport = new TransactionReport();
        Statistics statistics= transactionReport.generateReport("data/database.csv");

        String fmtTotalTransaction = integerFormatter.format(statistics.totalTransactions());
        String fmtTotalFrauds = integerFormatter.format(statistics.totalFrauds());
        String fmtTotalAmount = currencyFormatter.format(statistics.totalAmount());
        //String formattedTotalAmount = integerFormatter.format(statistics.totalAmount());

        String msgTotalTransactions = resourceBundle.getString("label.total.transactions");
        String msgTotalFrauds = resourceBundle.getString("label.total.frauds");
        String msgTotalAmount = resourceBundle.getString("label.total.amount");

        IO.println("""
                %s: %s
                %s: %s
                %s: %s
                """
        .formatted(msgTotalTransactions, fmtTotalTransaction,
                msgTotalFrauds, fmtTotalFrauds,
                msgTotalAmount, fmtTotalAmount));
    }
}
