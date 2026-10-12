package com.BankApp.Bank.dto;

import com.BankApp.Bank.model.Account;

import java.time.LocalDateTime;

public class TransactionResponse {

    private Long id;
    private double amount;
    private String type;
    private LocalDateTime timeStamp;

    public TransactionResponse() {
    }

    public TransactionResponse(Long id, double amount, String type, LocalDateTime timeStamp) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.timeStamp = timeStamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

}
