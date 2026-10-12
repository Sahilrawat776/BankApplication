package com.BankApp.Bank.service;

import com.BankApp.Bank.dto.*;
import com.BankApp.Bank.exception.AccountNotFoundException;
import com.BankApp.Bank.exception.InsufficientBalanceException;
import com.BankApp.Bank.exception.SameAccountException;
import com.BankApp.Bank.model.Transaction;
import com.BankApp.Bank.repository.AccountRepository;
import com.BankApp.Bank.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import com.BankApp.Bank.model.Account;
import org.springframework.transaction.annotation.Transactional;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BankService {

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    public BankService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
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
    public AccountResponse transferMoney(TransferMoneyRequest request) {

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

        Account savedAccount = accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        return new AccountResponse(
                savedAccount.getId(),
                savedAccount.getAccountHolderName(),
                savedAccount.getBalance()
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
