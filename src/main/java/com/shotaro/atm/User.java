package com.shotaro.atm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User {
    private String firstName;
    private String lastName;
    private String uuid;
    private byte[] pinHash;
    private ArrayList<Account> accounts;

    public User(String firstName, String lastName, String pin, Bank bank) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.uuid = bank.getNewUserUUID();

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.pinHash = md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error, caught noSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }

        accounts = new ArrayList<Account>();
        System.out.printf("New user %s %s with ID %s created. \n", firstName, lastName, uuid);
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public String getUUID() {
        return uuid;
    }

    public boolean validatePin(String pin) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(pin.getBytes()), this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error, caught NoSuchAlgorithmException.");
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }

    public void printAccountSummary() {
        System.out.printf("\n\n%s %s's account summary", this.firstName, this.lastName);
        for (int i = 0; i < this.accounts.size(); i++) {
            System.out.printf(" %d) %s", i+1, this.accounts.get(i).getAccountSummary());
        }
    }

    public int getAccountSize() {
        return this.accounts.size();
    }

    public Account getAccount(int accountIdx) {
        return this.accounts.get(accountIdx);
    }

    public double getAccountBalance(int accountNumber) {
        return this.accounts.get(accountNumber).getBalance();
    }

    public void addAccountTransaction(int accountIdx, double amount, String description) {
        this.getAccount(accountIdx).addTransaction(amount, description);
    }

    public void printTransactionHistory(int accountIdx) {
        this.getAccount(accountIdx).printTransactionHistory();
    }
}
