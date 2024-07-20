package com.compulynxtest.compulynxtest.auth;

import com.compulynxtest.compulynxtest.account.Account;
import com.compulynxtest.compulynxtest.account.AccountRepository;
import com.compulynxtest.compulynxtest.role.Role;
import com.compulynxtest.compulynxtest.role.RoleRepository;
import com.compulynxtest.compulynxtest.security.JwtService;
import com.compulynxtest.compulynxtest.user.Token;
import com.compulynxtest.compulynxtest.user.TokenRepository;
import com.compulynxtest.compulynxtest.user.User;
import com.compulynxtest.compulynxtest.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final AccountRepository accountRepository;

    private static final Random RANDOM = new SecureRandom();

    private String generateRandomAccountNumber() {
        // Generates a random 12-digit account number
        long accountNumber = (long) (RANDOM.nextDouble() * 10000000000L);
        return String.format("%012d", accountNumber);
    }

    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = generateRandomAccountNumber();
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }
    private String generateRandomPin() {
        int pin = RANDOM.nextInt(10000); // Generates a number between 0 and 9999
        return String.format("%04d", pin); // Ensures it's a 4-digit number
    }
    @Transactional
    public RegistrationResponse register(RegistrationRequest request) throws Exception {
        System.out.println(request);
        // Check if email or ID number already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateKeyException("Email already exists");
        }
        if (userRepository.existsByIdno(request.getIdno())) {
            throw new DuplicateKeyException("Customer ID Number already exists");
        }
        var pin = generateRandomPin();
        Role userRole = roleRepository.findByName("CUSTOMER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("CUSTOMER");
                    return roleRepository.save(newRole);
                });

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .middlename(request.getMiddlename())
                .email(request.getEmail())
                .idno(request.getIdno())
                .pin(passwordEncoder.encode(pin))
                .enabled(true)
                .roles(List.of(userRole))
                .build();
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("EMAIL")) {
                throw new DuplicateKeyException("Email already exists");
            } else if (e.getMessage().contains("idno")) {
                throw new DuplicateKeyException("Customer ID Number already exists");
            } else {
                throw e; // Rethrow other data integrity violations
            }
        }

        Account account = Account.builder()
                .accountNumber(generateUniqueAccountNumber())
                .customer(user)
                .bankName("Sample Bank")
                .accountType("checking_account")
                .active(true)
                .branchName("Sample Branch")
                .balance(0.0)
                .active(true)
                .build();

        // Save the account
        accountRepository.save(account);
        var message = "Registration Successful. Thank You For Creating an Account with Us. Your Pin is "
                + pin + ". Use Your ID Number and this Pin to Login";

        return RegistrationResponse.builder()
                .message(message)
                .build();
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
            var auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getIdno(),
                            request.getPin()
                    )
            );

            var claims = new HashMap<String, Object>();
            var user = ((User) auth.getPrincipal());
            claims.put("fullName", user.getFullName());

            var jwtToken = jwtService.generateToken(claims, (User) auth.getPrincipal());

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
    }


    private String generateAndSaveActivationToken(User user) {
        // Generate a token
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);

        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }
}
