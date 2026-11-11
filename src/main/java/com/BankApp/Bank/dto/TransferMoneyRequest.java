package com.BankApp.Bank.dto;

import jakarta.validation.constraints.Positive;

public class TransferMoneyRequest {
    @Positive
    private double amount;
    @Positive
    private Long fromAcc;
    @Positive
    private Long toAcc;

    private String idempotencyKey;

    public TransferMoneyRequest() {
    }

    public TransferMoneyRequest(double amount, Long fromAcc, Long toAcc, String idempotencyKey) {
        this.amount = amount;
        this.fromAcc = fromAcc;
        this.toAcc = toAcc;
        this.idempotencyKey = idempotencyKey;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Long getFromAcc() {
        return fromAcc;
    }

    public void setFromAcc(Long fromAcc) {
        this.fromAcc = fromAcc;
    }

    public Long getToAcc() {
        return toAcc;
    }

    public void setToAcc(Long toAcc) {
        this.toAcc = toAcc;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }
}
