package com.compulynxtest.compulynxtest.deposit;

import com.compulynxtest.compulynxtest.account.Account;
import com.compulynxtest.compulynxtest.account.AccountRepository;
import com.compulynxtest.compulynxtest.transaction.AccountTransactionRepository;
import com.compulynxtest.compulynxtest.transaction.AccountTransactions;
import com.compulynxtest.compulynxtest.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DepositService {
    private final AccountRepository accountRepository;
    private final AccountTransactionRepository transactionHistoryRepository;

    @Transactional
    public DepositResponse deposit(User user, Double amount) {
        Account account = accountRepository.findByCustomerId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found for CUSTOMER_ID: " + user.getId()));

        // Update account balance
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        // Create a transaction record
        AccountTransactions transaction = new AccountTransactions();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setTransactionType(AccountTransactions.TransactionType.DEPOSIT);
        transaction.setStatus(AccountTransactions.TransactionStatus.COMPLETED);
        transaction.setDescription("Deposit");
        transaction.setBalanceAfter(account.getBalance());
        transaction.setReference(java.util.UUID.randomUUID().toString());

        // Save transaction record
        transactionHistoryRepository.save(transaction);

        return new DepositResponse(
                transaction.getTransactionType().name(),
                transaction.getStatus().name(),
                transaction.getReference(),
                account.getBalance()
        );
    }
}
