package com.compulynxtest.compulynxtest.withdrawal;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class WithdrawalResponse {

    private String transactionType;
    private String status;
    private String reference;
    private Double newBalance;
}
