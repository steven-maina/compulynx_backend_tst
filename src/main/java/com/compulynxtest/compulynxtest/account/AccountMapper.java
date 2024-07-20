package com.compulynxtest.compulynxtest.account;


import com.compulynxtest.compulynxtest.transaction.AccountTransactions;
import org.springframework.stereotype.Service;

@Service
public class AccountMapper {

    public Account toAccount(AccountRequest request) {
        return Account.builder()
                .id(request.id())
                .accountNumber(request.accountNumber())
                .accountType(request.accountType())
                .bankName(request.bankName())
                .branchName(request.branchName())
                .swiftCode(request.swiftCode())
                .balance(request.balance())
                .active(request.active())
                .build();
    }
    public AccountBalanceResponse toAccountBalance(Double balance) {
        return AccountBalanceResponse.builder()
                .balance(balance)
                .build();
    }
    public WithdrawalAccountResponse toWithdrawalAccountResponse(AccountTransactions history) {
        return WithdrawalAccountResponse.builder()
                .id(history.getAccount().getId())
                .accountNumber(history.getAccount().getAccountNumber())
                .accountType(history.getAccount().getAccountType())
                .bankName(history.getAccount().getBankName())
                .branchName(history.getAccount().getBranchName())
                .balance(history.getAccount().getBalance())
                .build();
    }

    public AccountResponse toAccountResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .bankName(account.getBankName())
                .branchName(account.getBranchName())
                .balance(account.getBalance())
                .active(account.isActive())
                .customer(account.getCustomer().fullName())
                .build();
    }
}
