package com.chinyangatl.github.model;

/**
 *
 * Simple basic java object class to represent an Customer as specified on the backend.
 *
 * @author: Lesley Tsame Chinyanga, cse20-030
 * @version: 1.0
 */

public class Customer {
    private int customerId;
    private String firstName;
    private String surname;
    private String address;
    private String employer;
    private int age;

    public Customer () {
        this.customerId = generateCustomerID();
    }

    public Customer(String firstName, String surname, String address, String employer, int age) {
        this.customerId = generateCustomerID();
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.employer = employer;
        this.age = age;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private int generateCustomerID() {
        int min = 1000000;
        int max = 9999999;
        int accNo = (int)(Math.random() * (max - min + 1) + min);
        return accNo;
    }

    @Override
    public String toString() {
        return "Customer ID: " + getCustomerId() +
                ", First Name: " + getFirstName() +
                ", Surname: " + getSurname() +
                ", Address: " + getAddress() +
                ", Employer: " + getEmployer() +
                ", Age: " + getAge();
    }
}
