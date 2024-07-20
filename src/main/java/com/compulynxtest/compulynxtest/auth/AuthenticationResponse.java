package com.compulynxtest.compulynxtest.auth;

import com.compulynxtest.compulynxtest.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationResponse {
    private String token;
}
