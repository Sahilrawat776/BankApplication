package com.BankApp.Bank.service;

import com.BankApp.Bank.dto.*;
import com.BankApp.Bank.exception.AccountNotFoundException;
import com.BankApp.Bank.exception.InsufficientBalanceException;
import com.BankApp.Bank.exception.SameAccountException;
import com.BankApp.Bank.model.IdempotencyRecord;
import com.BankApp.Bank.model.Transaction;
import com.BankApp.Bank.model.Transfer;
import com.BankApp.Bank.repository.AccountRepository;
import com.BankApp.Bank.repository.IdempotencyRecordRepository;
import com.BankApp.Bank.repository.TransactionRepository;
import com.BankApp.Bank.repository.TransferRepository;
import org.springframework.stereotype.Service;
import com.BankApp.Bank.model.Account;
import org.springframework.transaction.annotation.Transactional;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BankService {

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    private final TransferRepository transferRepository;

    private final IdempotencyRecordRepository idempotencyRecordRepository;

    public BankService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            TransferRepository transferRepository,
            IdempotencyRecordRepository idempotencyRecordRepository) {

        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transferRepository = transferRepository;
        this.idempotencyRecordRepository = idempotencyRecordRepository;
    }





    public AccountResponse createAccount(CreateAccountRequest request){
        Account account = new Account(
                null,
                request.getAccountHolderName(),
                request.getBalance()
        );

        Account savedAccount =  accountRepository.save(account);
        AccountResponse response = new AccountResponse(
                savedAccount.getId(),
                savedAccount.getAccountHolderName(),
                savedAccount.getBalance()
        );

        return response;
    }

    public List<Account> getAllAccount(){
        return accountRepository.findAll();
    }

    public Account findAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found!!"));
    }

    @Transactional
    public AccountResponse deposit(DepositRequest request, Long id) {

        Account account = findAccount(id);

        account.setBalance(
                account.getBalance() + request.getAmount()
        );

        Account savedAccount = accountRepository.save(account);

        Transaction transaction = new Transaction(null,
                request.getAmount(), "DEPOSIT", LocalDateTime.now(), account
        );

        Transaction savedTransaction = transactionRepository.save(transaction);

        return new AccountResponse(
                savedAccount.getId(),
                savedAccount.getAccountHolderName(),
                savedAccount.getBalance()
        );
    }
    @Transactional
    public AccountResponse withdraw(WithdrawMoneyRequest request, Long id) {
        Account account = findAccount(id);

        if (account.getBalance() < request.getAmount()) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        account.setBalance(
                account.getBalance() - request.getAmount()
        );

        Account savedAccount = accountRepository.save(account);

        Transaction transaction = new Transaction(null,
                request.getAmount(), "WITHDRAW", LocalDateTime.now(), account
        );

        Transaction savedTransaction = transactionRepository.save(transaction);

        return new AccountResponse(
                savedAccount.getId(),
                savedAccount.getAccountHolderName(),
                savedAccount.getBalance()
        );
    }

    public Account dltAccount(Long id) {
        Account account = findAccount(id);

        accountRepository.delete(account);

        return account;
    }

    @Transactional
    public TransferResponse transferMoney(TransferMoneyRequest request) {

        Optional<IdempotencyRecord> existingRecord =
                idempotencyRecordRepository
                        .findByIdempotencyKey(request.getIdempotencyKey());

        if(existingRecord.isPresent()){
            IdempotencyRecord record = existingRecord.get();
            Long transferId = record.getTransferId();

            Transfer existingTransfer = transferRepository.findById(transferId)
                    .orElseThrow(() ->
                            new IllegalStateException("Original transfer not found"));

            return new TransferResponse(
                    existingTransfer.getId(),
                    existingTransfer.getFromAccount().getId(),
                    existingTransfer.getToAccount().getId(),
                    existingTransfer.getAmount(),
                    existingTransfer.getStatus(),
                    existingTransfer.getTimeStamp()
            );
        }

        if (request.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        if(request.getFromAcc().equals(request.getToAcc())){
            throw new SameAccountException("Same account!!");
        }


        Account fromAccount = findAccount(request.getFromAcc());
        Account toAccount = findAccount(request.getToAcc());

        if (fromAccount.getBalance() < request.getAmount()) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        fromAccount.setBalance(
                fromAccount.getBalance() - request.getAmount()
        );

        toAccount.setBalance(
                toAccount.getBalance() + request.getAmount()
        );

        Transaction transferOut = new Transaction(
                null,
                request.getAmount(),
                "TRANSFER_OUT",
                LocalDateTime.now(),
                fromAccount
        );

        Transaction transferIn = new Transaction(
                null,
                request.getAmount(),
                "TRANSFER_IN",
                LocalDateTime.now(),
                toAccount
        );

        Transfer transfer = new Transfer(null,
                fromAccount,
                toAccount,
                request.getAmount(),
                "SUCCESS",
                LocalDateTime.now()
        );

        Account savedAccount = accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        transactionRepository.save(transferOut);
        transactionRepository.save(transferIn);


        Transfer savedTransfer = transferRepository.save(transfer);

        IdempotencyRecord newRecord = new IdempotencyRecord(
                null,
                request.getIdempotencyKey(),
                savedTransfer.getId(),
                savedTransfer.getStatus()
        );

        idempotencyRecordRepository.save(newRecord);

        return new TransferResponse(
                savedTransfer.getId(),
                savedTransfer.getFromAccount().getId(),
                savedTransfer.getToAccount().getId(),
                savedTransfer.getAmount(),
                savedTransfer.getStatus(),
                savedTransfer.getTimeStamp()
        );
    }

    public List<TransactionResponse> getTransactionHistory(Long accountId) {
        findAccount(accountId);

        List<Transaction> transactions =
                transactionRepository.findByAccountId(accountId);

        List<TransactionResponse> responses = new ArrayList<>();

        for (Transaction transaction : transactions) {

            TransactionResponse response = new TransactionResponse(
                    transaction.getId(),
                    transaction.getAmount(),
                    transaction.getType(),
                    transaction.getTimeStamp()
            );

            responses.add(response);
        }

        return responses;
    }

}
