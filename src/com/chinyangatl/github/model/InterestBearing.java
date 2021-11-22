package com.chinyangatl.github.model;

public class InterestBearing extends Account{
    public static final double INTEREST_RATE = 0.05;
    public InterestBearing() {
        this.setAccountType(AccountType.INTERESTBEARING);
    }
}
