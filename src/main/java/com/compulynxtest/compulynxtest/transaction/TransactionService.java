package com.compulynxtest.compulynxtest.transaction;

import com.compulynxtest.compulynxtest.account.Account;
import com.compulynxtest.compulynxtest.exception.NoTransactionsFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final AccountTransactionRepository transactionRepository;

    @Autowired
    public TransactionService(AccountTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    public List<AccountTransactionResponse> getLast10Transactions(Account account) {
        List<AccountTransactions> transactions = transactionRepository.findTop10ByAccountOrderByTransactionDateDesc(account);
        if (transactions.isEmpty()) {
           return Collections.emptyList();

        }
        return transactions.stream()
                .map(this::toTransactionResponse)
                .collect(Collectors.toList());
    }

    private AccountTransactionResponse toTransactionResponse(AccountTransactions transaction) {
        return AccountTransactionResponse.builder()
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType().name())
                .transactionStatus(transaction.getStatus().name())
                .transactionDate(transaction.getTransactionDate())
                .description(transaction.getDescription())
                .reference(transaction.getReference())
                .build();
    }

    public AccountTransactionResponse findTransactionById(Long transactionId) {
        AccountTransactions transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found, Please check and try again"));
        return mapTransactionToDTO(transaction);
    }
    private AccountTransactionResponse mapTransactionToDTO(AccountTransactions transaction) {
        return new AccountTransactionResponse(
                transaction.getAmount(),
                transaction.getTransactionType().name(),
                transaction.getStatus().name(),
                transaction.getTransactionDate(),
                transaction.getDescription(),
                transaction.getReference());
    }
}
