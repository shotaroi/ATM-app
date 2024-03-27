package com.shotaro.atm;

import java.util.Date;

public class Transaction {
     private double amount;
     private Date timestamp;
     private String description;
     private Account account;

     public Transaction(double amount, Account account) {
          this.amount = amount;
          this.account = account;
          this.timestamp = new Date();
          this.description = "";
     }

     public Transaction(double amount, String description, Account account) {
          this(amount, account);
          this.description = description;
     }

     public double getAmount() {
          return this.amount;
     }

     public String printHistory() {
          return String.format("%s: %.02f: %s", this.timestamp, this.amount, this.description);
     }
}
