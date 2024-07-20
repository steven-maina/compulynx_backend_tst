package com.compulynxtest.compulynxtest.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationRequest {

    @NotEmpty(message = "ID number is mandatory")
    @NotNull(message = "ID number is mandatory")
    private String idno;

    @NotEmpty(message = "Pin is mandatory")
    @NotNull(message = "Pin is mandatory")
    @Size(min = 4, message = "Pin should be 4 characters long")
    private String pin;
}
