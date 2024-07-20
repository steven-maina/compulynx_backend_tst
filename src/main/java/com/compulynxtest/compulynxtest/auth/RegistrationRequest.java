package com.compulynxtest.compulynxtest.auth;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

@Getter
@Setter
@Builder
public class RegistrationRequest {


    @NotEmpty(message = "Firstname is mandatory")
    @NotNull(message = "Firstname is mandatory")
    private String firstname;

    private String middlename;

    @NotEmpty(message = "Lastname is mandatory")
    @NotNull(message = "Lastname is mandatory")
    private String lastname;
    @Email(message = "Email is not well formatted")
    @NotEmpty(message = "Email is mandatory")
    @NotNull(message = "Email is mandatory")
    private String email;
    @NotEmpty(message = "ID Number is mandatory")
    @NotNull(message = "ID Number is mandatory")
    @Pattern(regexp = "\\d+", message = "ID Number must be a number")
    @Size(min = 5, message = "ID Number should be at least 5 characters long")
    private String idno;
}
