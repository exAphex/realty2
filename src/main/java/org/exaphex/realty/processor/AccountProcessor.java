package org.exaphex.realty.processor;

import org.exaphex.realty.db.service.TransactionService;
import org.exaphex.realty.model.Account;
import org.exaphex.realty.model.Transaction;

import java.util.List;

import static org.exaphex.realty.model.Transaction.isExpense;

public class AccountProcessor {
    public static float calculateAccountBalance(Account account) {
        List<Transaction> transactions = TransactionService.getTransactionsByAccount(account);
        float fTotal = 0f;
        for (Transaction t : transactions) {
            if (isExpense(t.getType())) {
                fTotal -= t.getAmount() + t.getSecondaryAmount();
            } else {
                fTotal += t.getAmount() + t.getSecondaryAmount();
            }
        }
        return fTotal;
    }
}
