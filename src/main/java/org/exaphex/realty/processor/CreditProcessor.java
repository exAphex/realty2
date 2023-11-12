package org.exaphex.realty.processor;

import org.exaphex.realty.model.Credit;
import org.exaphex.realty.model.Transaction;

import java.util.List;

public class CreditProcessor {
    public static float getTotalPaidAmount(List<Transaction> transactions) {
        return transactions.stream().filter(e -> e.getType() == Transaction.CREDIT_PAYMENT).map(Transaction::getAmount).reduce(0f, Float::sum);
    }

    public static float getPaidAmount(Credit credit, List<Transaction> transactions) {
        return transactions.stream().filter(e -> e.getType() == Transaction.CREDIT_PAYMENT).filter(e -> credit.getId().equals(e.getReference())).map(Transaction::getAmount).reduce(0f, Float::sum);
    }

    public static float getTotalAmount(List<Credit> credits) {
        return credits.stream().map(Credit::getAmount).reduce(0f, Float::sum);
    }
}
