package com.compulynxtest.compulynxtest.deposit;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepositResponse {

    private String transactionType;
    private String status;
    private String reference;
    private Double newBalance;
}

