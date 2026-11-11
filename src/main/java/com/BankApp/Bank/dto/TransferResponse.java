package com.BankApp.Bank.dto;

import java.time.LocalDateTime;

public class TransferResponse {

    private Long transferID;
    private Long senderID;
    private Long receiverID;
    private double amount;
    private String status;
    private LocalDateTime timeStamp;


    public TransferResponse() {
    }

    public TransferResponse(Long transferID, Long senderID, Long receiverID, double amount, String status, LocalDateTime timeStamp) {
        this.transferID = transferID;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.amount = amount;
        this.status = status;
        this.timeStamp = timeStamp;
    }

    public Long getTransferID() {
        return transferID;
    }

    public void setTransferID(Long transferID) {
        this.transferID = transferID;
    }

    public Long getSenderID() {
        return senderID;
    }

    public void setSenderID(Long senderID) {
        this.senderID = senderID;
    }

    public Long getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(Long receiverID) {
        this.receiverID = receiverID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
