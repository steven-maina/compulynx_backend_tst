package com.compulynxtest.compulynxtest.transfer;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TransferResponse {
    private String message;
    private String transactionType;
    private String status;
    private String reference;
    private Double newBalance;
}
