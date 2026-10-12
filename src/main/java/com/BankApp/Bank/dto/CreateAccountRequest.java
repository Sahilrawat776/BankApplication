package com.BankApp.Bank.dto;

public class CreateAccountRequest {
    private String accountHolderName;
    private double balance;

    public CreateAccountRequest() {
    }

    public CreateAccountRequest(String accountHolderName, double balance) {
        this.accountHolderName = accountHolderName;
        this.balance = balance;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
