package com.compulynxtest.compulynxtest.account;

import com.compulynxtest.compulynxtest.deposit.DepositRequest;
import com.compulynxtest.compulynxtest.deposit.DepositResponse;
import com.compulynxtest.compulynxtest.deposit.DepositService;
import com.compulynxtest.compulynxtest.transaction.AccountTransactionResponse;
import com.compulynxtest.compulynxtest.transaction.AccountTransactions;
import com.compulynxtest.compulynxtest.transaction.TransactionService;
import com.compulynxtest.compulynxtest.transfer.TransferRequest;
import com.compulynxtest.compulynxtest.transfer.TransferResponse;
import com.compulynxtest.compulynxtest.transfer.TransferService;
import com.compulynxtest.compulynxtest.user.User;
import com.compulynxtest.compulynxtest.withdrawal.WithdrawalRequest;
import com.compulynxtest.compulynxtest.withdrawal.WithdrawalResponse;
import com.compulynxtest.compulynxtest.withdrawal.WithdrawalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("accounts")
@RequiredArgsConstructor
@Tag(name = "Account")
public class AccountController {

    private final AccountService userService;
    private final AccountMapper accountMapper;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final DepositService depositService;
    private final WithdrawalService withdrawalService;
    private final TransferService transferService;

    @GetMapping("/balance")
    public ResponseEntity<AccountBalanceResponse> getAccountBalance() {
        User user = userService.getUser();
        Double balance = accountService.getAccountBalance(user.getId());
        // Map balance to AccountBalanceResponse
        AccountBalanceResponse response = accountMapper.toAccountBalance(balance);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<AccountTransactionResponse>> getLast10Transactions() {
        // Get authenticated user
        User user = userService.getUser();

        // Retrieve account for the user
        Account account = accountService.getAccountByCustomerId(user.getId());

        // Get the last 10 transactions
        List<AccountTransactionResponse> transactions = transactionService.getLast10Transactions(account);
        if (transactions.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(transactions);
    }


    @PostMapping("/deposit")
    public ResponseEntity<DepositResponse> deposit(@RequestBody @Valid DepositRequest request) {
        // Get authenticated user
        User user = userService.getUser();

        // Perform deposit
        DepositResponse response = depositService.deposit(user, request.getAmount());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<WithdrawalResponse> withdraw(@RequestBody @Valid WithdrawalRequest request) {
        // Get authenticated user
        User user = userService.getUser();

        // Perform withdrawal
        WithdrawalResponse response = withdrawalService.withdraw(user, request.getAmount());

        return ResponseEntity.ok(response);
    }
    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transferFunds(@RequestBody @Valid TransferRequest request) {
        // Get authenticated user
        User user = userService.getUser();

        // Perform funds transfer and get updated balance
        TransferResponse newBalance = transferService.transfer(user, request);

        return ResponseEntity.ok(newBalance);
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<AccountTransactionResponse> getTransactionById(@PathVariable Long transactionId) {
        AccountTransactionResponse transaction = transactionService.findTransactionById(transactionId);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/user")
    public ResponseEntity<String> getUserInfo() {
        User user = userService.getUser();
        return ResponseEntity.ok(user.fullName());
    }

    @GetMapping("/{account-id}")
    public ResponseEntity<AccountResponse> findAccountById(
            @PathVariable("account-id") Long accountId
    ) {
        return ResponseEntity.ok(userService.findById(accountId));
    }

    @PostMapping
    public ResponseEntity<Long> saveAccount(
            @Valid @RequestBody AccountRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(userService.save(request, connectedUser));
    }

}
