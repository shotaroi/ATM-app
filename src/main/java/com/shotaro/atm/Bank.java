package com.shotaro.atm;

import java.util.ArrayList;
import java.util.Random;

public class Bank {
    private String name;
    private ArrayList<User> users;
    private ArrayList<Account> accounts;

    public Bank(String name) {
        this.name = name;
        this.users = new ArrayList<User>();
        this.accounts = new ArrayList<Account>();
    }

    public String uuidGenerateHelper(int idLength) {
        StringBuilder uuid = new StringBuilder();
        Random r = new Random();
        int len = 10;
        boolean unique;

        do {
            for (int i = 0; i < len; i++) {
                uuid.append(((Integer) r.nextInt(10)).toString());
            }
            unique = true;
            for (User user : users) {
                if (user.getUUID().contains(uuid)) {
                    unique = false;
                    break;
                }
            }
        } while (!unique);

        return uuid.toString();
    }

    public String getNewUserUUID() {
        int idLength = 10;
        return uuidGenerateHelper(idLength);
    }

    public String getNewAccountUUID() {
        int idLength = 10;
        return uuidGenerateHelper(idLength);
    }

    public User addUser(String firstName, String lastName, String pin) {
        User user = new User(firstName, lastName, pin, this);
        this.users.add(user);

        Account account = new Account("Savings", user, this);
        user.addAccount(account);
        this.accounts.add(account);

        return user;
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    public User userLogin(String userID, String pin) {
        for (User user : this.users) {
            if (user.getUUID().contains(userID) && user.validatePin(pin)) {
                return user;
            }
        }

        return null;
    }

    public String getName() {
        return this.name;
    }
}
