package com.compulynxtest.compulynxtest.transaction;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class AccountTransactionResponse {
    private String message;
    private Double amount;
    private String transactionType;
    private String transactionStatus;
    private LocalDateTime transactionDate;
    private String description;
    private String reference;

    public AccountTransactionResponse( Double amount, String transactionType, String transactionStatus, LocalDateTime transactionDate, String description, String reference) {
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionStatus = transactionStatus;
        this.transactionDate = transactionDate;
        this.description = description;
        this.reference = reference;
    }
    public AccountTransactionResponse(String message) {
        this.message = message;
    }

}
