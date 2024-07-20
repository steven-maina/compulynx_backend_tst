package com.compulynxtest.compulynxtest.transaction;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountTransactionRequest {
    @NotNull(message = "Amount is mandatory")
    @Positive(message = "Amount must be positive")
    private Double amount;
}
