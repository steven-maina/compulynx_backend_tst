package com.compulynxtest.compulynxtest.account;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WithdrawalAccountResponse {
    private Long id;
    private String accountNumber;
    private String accountType;
    private String bankName;
    private String branchName;
    private double balance;
    private String customer;
}
