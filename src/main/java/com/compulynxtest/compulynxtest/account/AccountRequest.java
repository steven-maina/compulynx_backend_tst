package com.compulynxtest.compulynxtest.account;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AccountRequest(
        Long id,
        @NotNull(message = "100")
        @NotEmpty(message = "100")
        String accountNumber,
        @NotNull(message = "101")
        @NotEmpty(message = "101")
        String accountType,
        @NotNull(message = "102")
        @NotEmpty(message = "102")
        String bankName,
        @NotNull(message = "103")
        @NotEmpty(message = "103")
        String branchName,
        @NotNull(message = "104")
        @NotEmpty(message = "104")
        String swiftCode,
        double balance,
        boolean active,
        boolean shareable
) {
}
