package com.compulynxtest.compulynxtest.transaction;

import com.compulynxtest.compulynxtest.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountTransactionRepository extends JpaRepository<AccountTransactions, Long> {
    List<AccountTransactions> findTop10ByAccountOrderByTransactionDateDesc(Account account);
    Optional<AccountTransactions> findAccountTransactionsById(Long id);
}
