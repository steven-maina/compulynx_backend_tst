package com.compulynxtest.compulynxtest.withdrawal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WithdrawalRequest {
    @NotNull(message = "Amount is mandatory")
    @Positive(message = "Amount must be positive")
    private Double amount;
}
