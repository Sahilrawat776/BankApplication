package com.BankApp.Bank.controller;

import com.BankApp.Bank.dto.*;
import com.BankApp.Bank.model.Account;
import com.BankApp.Bank.service.BankService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BankController {
    private final BankService bankService;

    //    Constructor Dependency Injection
    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping("/accounts")
    public AccountResponse createAccount(@Valid @RequestBody CreateAccountRequest request){
        return bankService.createAccount(request);
    }

    @GetMapping("/accounts")
    public List<Account> getAllAccount(){
        return bankService.getAllAccount();
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long id) {
        Account account = bankService.findAccount(id);

        AccountResponse response = new AccountResponse(
                account.getId(),
                account.getAccountHolderName(),
                account.getBalance()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/accounts/{id}/deposit")
    public ResponseEntity<AccountResponse> deposit(@Valid @RequestBody DepositRequest request, @PathVariable Long id){
        AccountResponse response = bankService.deposit(request, id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/accounts/{id}/withdraw")
    public ResponseEntity<AccountResponse> withdraw(@Valid @RequestBody WithdrawMoneyRequest request, @PathVariable Long id){
        AccountResponse response = bankService.withdraw(request, id);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<Account> dltAccount(@PathVariable Long id) {
        Account account = bankService.dltAccount(id);

            return ResponseEntity.ok(account);
    }

    @PostMapping("/accounts/transfers")
    public ResponseEntity<AccountResponse> transferMoney(@Valid @RequestBody TransferMoneyRequest request) {

        AccountResponse response = bankService.transferMoney(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/accounts/{id}/transactions")
    public List<TransactionResponse> getTransactionHistory(
            @PathVariable Long id) {

        return bankService.getTransactionHistory(id);
    }

}
