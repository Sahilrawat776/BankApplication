package com.BankApp.Bank.dto;

import jakarta.validation.constraints.Positive;

public class WithdrawMoneyRequest {
    @Positive
    private double amount;


    public WithdrawMoneyRequest() {
    }

    public WithdrawMoneyRequest(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
