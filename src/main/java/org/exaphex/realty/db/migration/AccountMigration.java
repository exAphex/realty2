package org.exaphex.realty.db.migration;

import org.exaphex.realty.db.service.AccountService;
import org.exaphex.realty.db.service.TransactionService;
import org.exaphex.realty.model.Account;
import org.exaphex.realty.model.Transaction;

import java.util.List;

public class AccountMigration {

    public static void migrateTransactionToAccounts() {
        List<Transaction> transactions = TransactionService.getTransactions();
        Account acc = new Account("Main", "", "");
        AccountService.addAccount(acc);
        for (Transaction t : transactions) {
            t.setAccountId(acc.getId());
            TransactionService.updateTransaction(t);
        }
    }
}
