package com.chinyangatl.github.model;

/**
 *
 * Simple basic java object class to represent an Transaction as specified on the backend.
 * Transactions can be of type DEPOSIT or WITHDRAW as seen in the enums.
 * A Timestamp is used to track when a transaction type is created.
 * It is only instantiated when an Account calls its withdraw or deposit method.
 *
 * @author: Lesley Tsame Chinyanga, cse20-030
 * @version: 1.0
 */

import java.sql.Timestamp;

public class Transaction {
    //private ObjectProperty<Enum<TransactionType>> type;
    private int accountNo, customerID;
    private TransactionType type;
    private double amount;
    private Timestamp datetime;


    public enum TransactionType {
        DEPOSIT,
        WITHDRAW
    }

    public Transaction() {}

    public Transaction(int accountNo, int customerID, TransactionType type, double amount, Timestamp datetime) {
        this.accountNo = accountNo;
        this.customerID = customerID;
        this.type = type;
        this.amount = amount;
        this.datetime = datetime;
    }

    public int getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(int accountNo) {
        this.accountNo = accountNo;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }
}
