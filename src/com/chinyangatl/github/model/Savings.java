package com.chinyangatl.github.model;

public class Savings extends Account{
    public static final double INTEREST_RATE = 0.00005;

    public Savings() {
        this.setAccountType(AccountType.SAVINGS);
    }
}
