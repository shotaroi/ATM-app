package com.shotaro.atm;

import java.util.ArrayList;

public class Account {
    private String type;
    private String uuid;
    private User user;
    private ArrayList<Transaction> transactions;

    public Account(String type, User user, Bank bank) {
        this.type = type;
        this.user = user;
        this.uuid = bank.getNewAccountUUID();
        this.transactions = new ArrayList<Transaction>();
    }

    public double getBalance() {
        double balance = 0.0;
        for (Transaction t : transactions) {
            balance += t.getAmount();
        }
        return balance;
    }

    public String getAccountSummary() {
        double balance = this.getBalance();
        return String.format("%s: %s: $%.02f", this.type, this.uuid, balance);
    }

    public String getUUID() {
        return this.uuid;
    }

    public void addTransaction(double amount, String description) {
        Transaction transaction = new Transaction(amount, description, this);
        this.transactions.add(transaction);
    }

    public void printTransactionHistory() {
        System.out.printf("\nTransaction history for the account: %s: %s\n", this.type, this.uuid);
        for (Transaction transaction : this.transactions) {
            System.out.println(transaction.printHistory());
        }
    }
}
