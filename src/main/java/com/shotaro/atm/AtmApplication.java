package com.shotaro.atm;

import java.util.Scanner;

public class AtmApplication {
    public static void main(String[] args) {
        Scanner sc  = new Scanner(System.in);
        Bank bank = new Bank("Bank of Sweden");
        User user = bank.addUser("Carl", "Gustav", "1234");
        Account account = new Account("Checking", user, bank);
        bank.addAccount(account);
        user.addAccount(account);

        while (true) {
            User authenticatedUser = AtmApplication.mainMenuPrompt(bank, sc);
            AtmApplication.printUserMenu(authenticatedUser, sc);
        }
    }

    private static User mainMenuPrompt(Bank bank, Scanner sc) {
        String userID;
        String pin;
        User authenticatedUser;

        do {
            System.out.printf("\n\nWelcome to %s\n\n", bank.getName());
            System.out.println("Enter user ID: ");
            userID = sc.nextLine();
            System.out.println("Enter pin: ");
            pin = sc.nextLine();
            authenticatedUser = bank.userLogin(userID, pin);

            if (authenticatedUser == null) {
                System.out.println("Invalid user ID or user pin. Please try again.");
            }
        } while (authenticatedUser == null);

        return authenticatedUser;
    }

    private static void printUserMenu(User user, Scanner sc) {
        user.printAccountSummary();
        int selectedOption;

        do {
            System.out.println("\n\nSelect an option");
            System.out.println(" 1: Show account transaction history");
            System.out.println(" 2: Withdrawal");
            System.out.println(" 3: Deposit");
            System.out.println(" 4: Transfer");
            System.out.println(" 5: Quit");
            selectedOption = sc.nextInt();
            if (selectedOption > 5 || selectedOption < 1) {
                System.out.println(" Select a correct option");
            }
        } while (selectedOption > 5 || selectedOption < 1);

        switch (selectedOption) {
            case 1 -> AtmApplication.showTransactionHistory(user, sc);
            case 2 -> AtmApplication.withdraw(user, sc);
            case 3 -> AtmApplication.deposit(user, sc);
            case 4 -> AtmApplication.transfer(user, sc);
            case 5 -> sc.nextLine();
        }

        if (selectedOption != 5) {
            AtmApplication.printUserMenu(user, sc);
        }
    }

    private static void showTransactionHistory(User user, Scanner sc) {
        int accountNumber;

        do {
            System.out.printf("Enter the account number (1-%d) for the transaction history.\n", user.getAccountSize());
            accountNumber = sc.nextInt();
            if (invalidAccountNumber(accountNumber, user)) {
                System.out.println("Invalid account number.");
            }
        } while (invalidAccountNumber(accountNumber, user));

        user.printTransactionHistory(accountNumber-1);
    }

    private static void withdraw(User user, Scanner sc) {
        int accountNumber;
        int accountIdx;
        double amount;
        double accountBalance;

        do {
            System.out.printf("Enter the account number (1-%d) for the withdrawal.\n", user.getAccountSize());
            accountNumber = sc.nextInt();
            if (invalidAccountNumber(accountNumber, user)) {
                System.out.println("Invalid account number.");
            }
        } while (invalidAccountNumber(accountNumber, user));

        accountBalance = user.getAccountBalance(accountNumber-1);

        do {
            System.out.printf("Enter the amount for the withdrawal (Max: $%.02f).\n", accountBalance);
            amount = sc.nextDouble();
            if (invalidAmount(amount, accountBalance)) {
                System.out.println("Invalid amount. Please try again.");
            }
        } while (invalidAmount(amount, accountBalance));

        accountIdx = accountNumber - 1;

        user.addAccountTransaction(accountIdx, -(amount), "Withdrawal");
    }

    private static void deposit(User user, Scanner sc) {
        int accountNumber;
        int accountIdx;
        double amount;

        do {
            System.out.printf("Enter the account number (1-%d) for the deposit.\n", user.getAccountSize());
            accountNumber = sc.nextInt();
            if (invalidAccountNumber(accountNumber, user)) {
                System.out.println("Invalid account number.");
            }
        } while (invalidAccountNumber(accountNumber, user));

        do {
            System.out.println("Enter the amount for the deposit.");
            amount = sc.nextDouble();
            if (amount > 1000) {
                System.out.println("The amount is too high. Please try again.");
            }
        } while (amount > 1000);

        accountIdx = accountNumber - 1;

        user.addAccountTransaction(accountIdx, amount, "deposit");
    }

    private static void transfer(User user, Scanner sc) {
        int fromAccountNumber;
        int toAccountNumber;
        int fromAccountIdx;
        int toAccountIdx;
        double amount;

        do {
            System.out.printf("Choose an account (1-%d) to transfer from.\n", user.getAccountSize());
            fromAccountNumber = sc.nextInt();
            if (invalidAccountNumber(fromAccountNumber, user)) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (invalidAccountNumber(fromAccountNumber, user));

        do {
            System.out.printf("Choose an account (1-%d) to transfer to.\n", user.getAccountSize());
            toAccountNumber = sc.nextInt();
            if (invalidAccountNumber(toAccountNumber, user)) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (invalidAccountNumber(toAccountNumber, user));

        double accountBalance = user.getAccountBalance(fromAccountNumber - 1);

        do {
            System.out.printf("Enter the amount for the transfer (Max: $%.02f).\n", accountBalance);
            amount = sc.nextDouble();
            if (invalidAmount(amount, accountBalance)) {
                System.out.println("Invalid amount. Please try again.");
            }
        } while (invalidAmount(amount, accountBalance));

        fromAccountIdx = fromAccountNumber - 1;
        toAccountIdx = toAccountNumber - 1;

        user.addAccountTransaction(fromAccountIdx, -(amount), String.format("Transfer to account %s.", user.getAccount(toAccountIdx).getUUID()));
        user.addAccountTransaction(toAccountIdx, amount, String.format("Transfer from account %s.", user.getAccount(fromAccountIdx).getUUID()));
    }

    private static boolean invalidAccountNumber(int accountNumber, User user) {
        return accountNumber < 1 || accountNumber > user.getAccountSize();
    }

    private static boolean invalidAmount(double amount, double accountBalance) {
        return amount < 0 || amount > accountBalance;
    }

}
