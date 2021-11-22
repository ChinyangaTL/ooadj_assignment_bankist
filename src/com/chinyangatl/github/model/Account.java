package com.chinyangatl.github.model;

/**
 *
 * Simple basic java object class to represent an Account as specified on the backend.
 * Account can be of type SAVINGS, CHEQUE OR INTERESTBEARING as seen in the enums which can allow or disallow some features.
 * Accounts belong to an owner of type Customer.
 *
 * @author: Lesley Tsame Chinyanga, cse20-030
 * @version: 1.0
 */

import java.sql.SQLException;

public class Account {
    private int accountNo;
    private int pinNo;
    private double balance;
    private Customer owner;
    private AccountType accountType;

    public enum AccountType {
        SAVINGS,
        CHEQUE,
        INTERESTBEARING
    }

    public Account () {}

    public Account(int accountNo, int pinNo, double balance, Customer owner, AccountType accountType) {
        this.accountNo = accountNo;
        this.pinNo = pinNo;
        this.balance = balance;
        this.owner = owner;
        this.accountType = accountType;
    }

    public int getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(int accountNo) throws SQLException {
        this.accountNo = accountNo;
    }

    public int getPinNo() {
        return pinNo;
    }

    public void setPinNo(int pinNo) {
        this.pinNo = pinNo;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Customer getOwner() {
        return owner;
    }

    public void setOwner(Customer owner) {
        this.owner = owner;
    }

    public String getAccountType() {
        return accountType.toString();
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

//    public boolean deposit(double amount) {
//        if(amount > 0) {
//            this.balance += amount;
//            return true;
//        }
//        return false;
//    }

//    public boolean withdraw(double amount) {
//        if(amount > 0) {
//            this.balance -= amount;
//            return true;
//        }
//        return false;
//    }

    public double checkBalance() {
        return balance;
    }
//
    public String requestStatement() {
        return "Account No: " + getAccountNo() + "\n" +
                "Owner: " + getOwner().getSurname() + ", " + getOwner().getFirstName() + "\n" +
                "Current Balance: " + getBalance();
    }

    public boolean changePin(int newPin) {
        if (String.valueOf(newPin).length() == 4) {
            this.pinNo = newPin;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Account No: " + accountNo +
                ", Pin No: " + pinNo +
                ", Balance: " + balance +
                ", Owner: " + owner.getCustomerId() +
                ", AccountType: " + accountType;
    }
}
