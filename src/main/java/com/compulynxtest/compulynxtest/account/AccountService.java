package com.compulynxtest.compulynxtest.account;


import com.compulynxtest.compulynxtest.transaction.AccountTransactionRepository;
import com.compulynxtest.compulynxtest.transaction.AccountTransactions;
import com.compulynxtest.compulynxtest.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final AccountTransactionRepository transactionHistoryRepository;

    public Long save(AccountRequest request, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Account account = accountMapper.toAccount(request);
        account.setCustomer(user);
        return accountRepository.save(account).getId();
    }

    public AccountResponse findById(Long accountId) {
        return accountRepository.findById(accountId)
                .map(accountMapper::toAccountResponse)
                .orElseThrow(() -> new EntityNotFoundException("No account found with ID:: " + accountId));
    }
    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public Double getAccountBalance(Long userId) {
        // retrieve account balance based on userId
        Optional<Account> accountOptional = accountRepository.findByCustomerId(userId);
        if (accountOptional.isPresent()) {
            return accountOptional.get().getBalance();
        } else {
            throw new RuntimeException("Account not found for user with ID: " + userId);
        }
    }

    public Account getAccountByCustomerId(Long userId) {
        return accountRepository.findByCustomerId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for user with ID: " + userId));
    }

}
