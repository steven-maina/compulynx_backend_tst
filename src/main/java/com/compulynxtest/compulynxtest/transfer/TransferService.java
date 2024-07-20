package com.compulynxtest.compulynxtest.transfer;

import com.compulynxtest.compulynxtest.account.Account;
import com.compulynxtest.compulynxtest.account.AccountRepository;
import com.compulynxtest.compulynxtest.transaction.AccountTransactionRepository;
import com.compulynxtest.compulynxtest.transaction.AccountTransactions;
import com.compulynxtest.compulynxtest.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferService {
private final AccountRepository accountRepository;
private final AccountTransactionRepository transactionRepository;
    @Transactional
    public TransferResponse transfer(User user, TransferRequest request) {
        // Get user's account
        Account senderAccount = accountRepository.findByCustomerId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));

        // Validate sufficient balance
        if (senderAccount.getBalance() < request.getAmount()) {
            throw new IllegalArgumentException("Insufficient balance for transfer");
        }

        // Get recipient account
        Account recipientAccount = accountRepository.findByAccountNumber(request.getRecipientAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Recipient account not found"));

        // Check if sender and recipient are the same account
        if (senderAccount.getId().equals(recipientAccount.getId())) {
            throw new IllegalArgumentException("Cannot transfer funds to the same account");
        }
        // Update sender's account balance
        senderAccount.setBalance(senderAccount.getBalance() - request.getAmount());
        accountRepository.save(senderAccount);

        // Update recipient's account balance
        recipientAccount.setBalance(recipientAccount.getBalance() + request.getAmount());
        accountRepository.save(recipientAccount);

        // Create transaction records
        AccountTransactions senderTransaction = createTransaction(senderAccount, request.getAmount(),
                AccountTransactions.TransactionType.TRANSFER, AccountTransactions.TransactionStatus.COMPLETED,
                "Transfer to " + recipientAccount.getAccountNumber());

        AccountTransactions recipientTransaction = createTransaction(recipientAccount, request.getAmount(),
                AccountTransactions.TransactionType.TRANSFER, AccountTransactions.TransactionStatus.COMPLETED,
                "Transfer from " + senderAccount.getAccountNumber());

        transactionRepository.save(senderTransaction);
        transactionRepository.save(recipientTransaction);
        var message = "Transfer of KES " + request.getAmount() + " to Account Number " + recipientAccount.getAccountNumber() + " was successful";
        return new TransferResponse(
                message.toUpperCase(),
                senderTransaction.getTransactionType().name(),
                senderTransaction.getStatus().name(),
                senderTransaction.getReference(),
                senderAccount.getBalance()
        );
    }

    private AccountTransactions createTransaction(Account account, Double amount,
                                                  AccountTransactions.TransactionType transactionType,
                                                  AccountTransactions.TransactionStatus status,
                                                  String description) {
        AccountTransactions transaction = new AccountTransactions();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);
        transaction.setStatus(status);
        transaction.setDescription(description);
        transaction.setBalanceAfter(account.getBalance());
        transaction.setReference(UUID.randomUUID().toString()); // Generate unique reference
        return transaction;
    }

}
