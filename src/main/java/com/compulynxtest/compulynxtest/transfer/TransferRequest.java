package com.compulynxtest.compulynxtest.transfer;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TransferRequest {

    @NotNull(message = "Amount is Required")
    @Positive(message = "Amount must be positive")
    private Double amount;
    @NotNull(message = "Recipient Account Number is Required")
    private String recipientAccountNumber;
}

