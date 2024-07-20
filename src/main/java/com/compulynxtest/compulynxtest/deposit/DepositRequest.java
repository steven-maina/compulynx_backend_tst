package com.compulynxtest.compulynxtest.deposit;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepositRequest {

    @NotNull(message = "Amount is mandatory")
    @Positive(message = "Amount must be positive")
    private Double amount;
}
