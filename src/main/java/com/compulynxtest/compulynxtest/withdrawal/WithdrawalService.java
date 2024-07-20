package com.compulynxtest.compulynxtest.withdrawal;

import com.compulynxtest.compulynxtest.account.Account;
import com.compulynxtest.compulynxtest.account.AccountRepository;
import com.compulynxtest.compulynxtest.transaction.AccountTransactionRepository;
import com.compulynxtest.compulynxtest.transaction.AccountTransactions;
import com.compulynxtest.compulynxtest.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WithdrawalService {
    private final AccountRepository accountRepository;
    private final AccountTransactionRepository transactionHistoryRepository;

    @Transactional
    public WithdrawalResponse withdraw(User user, Double amount) {
        Account account = accountRepository.findByCustomerId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found for CUSTOMER_ID: " + user.getId()));

        // Check if the account has sufficient balance
        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("You do not have enough balance to complete this transaction.");
        }

        // Update account balance
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        // Create a transaction record
        AccountTransactions transaction = new AccountTransactions();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setTransactionType(AccountTransactions.TransactionType.WITHDRAWAL);
        transaction.setStatus(AccountTransactions.TransactionStatus.COMPLETED);
        transaction.setDescription("Withdrawal");
        transaction.setBalanceAfter(account.getBalance());
        transaction.setReference(UUID.randomUUID().toString()); // Generate unique reference

        transactionHistoryRepository.save(transaction);

        return new WithdrawalResponse(
                transaction.getTransactionType().name(),
                transaction.getStatus().name(),
                transaction.getReference(),
                account.getBalance()
        );
    }
}
