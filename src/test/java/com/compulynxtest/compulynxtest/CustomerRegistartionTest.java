package com.compulynxtest.compulynxtest;

import com.compulynxtest.compulynxtest.account.AccountRepository;
import com.compulynxtest.compulynxtest.auth.AuthenticationService;
import com.compulynxtest.compulynxtest.auth.RegistrationRequest;
import com.compulynxtest.compulynxtest.auth.RegistrationResponse;
import com.compulynxtest.compulynxtest.role.Role;
import com.compulynxtest.compulynxtest.role.RoleRepository;
import com.compulynxtest.compulynxtest.user.UserRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerRegistartionTest {
        @Mock
        private UserRepository userRepository;

        @Mock
        private RoleRepository roleRepository;
        @Mock
        private PasswordEncoder passwordEncoder;

        @Mock
        private AccountRepository accountRepository;

        @InjectMocks
        private AuthenticationService userService;

        @BeforeEach
        public void setUp() {
            // Mock roleRepository's findByName method
            Role customerRole = new Role();
            customerRole.setName("CUSTOMER");
            when(roleRepository.findByName("CUSTOMER")).thenReturn(Optional.of(customerRole));

            // Mock userRepository methods
            when(userRepository.existsByIdno(anyString())).thenReturn(false);
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
        }

        @Test
        public void testRegisterNewCustomer() throws Exception {
            // Mock save method of userRepository and accountRepository
            when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
            when(accountRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            // Create a registration request
            when(passwordEncoder.encode("34341322221")).thenReturn("hashedPassword");

            // Create a registration request
            RegistrationRequest request = RegistrationRequest.builder()
                    .firstname("John")
                    .lastname("Doe")
                    .email("john.doe@example.com")
                    .idno("34341322221")
                    .build();

            // Call the register method in your service
            RegistrationResponse result = userService.register(request);

            // Verify userRepository methods were called
            verify(userRepository, times(1)).existsByIdno("34341322221");
            verify(userRepository, times(1)).save(any());

            // Verify accountRepository method was called
            verify(accountRepository, times(1)).save(any());

            // Assert the result message
            assertThat(result).toString().contains("Registration successful");
        }
    @Test
    public void testRegistrationRequestValidation() {
        // Create a valid RegistrationRequest object using Lombok @Builder
        RegistrationRequest request = RegistrationRequest.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .idno("12345")
                .build();

        // Validate the object using Bean Validation (JSR 380)
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        var violations = validator.validate(request);

        // Assert that there are no validation errors
        assertTrue(violations.isEmpty(), "Validation errors: " + violations);

        // Additional assertions if needed
        assertThat(request.getFirstname()).isEqualTo("John");
        assertThat(request.getLastname()).isEqualTo("Doe");
        assertThat(request.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(request.getIdno()).isEqualTo("12345");
    }
        @Test
        public void testRegisterUserWithExistingId() throws Exception {
            // Mock userRepository to return true for existsByIdno
            when(userRepository.existsByIdno("34341322221")).thenReturn(true);

            // Create a registration request
            RegistrationRequest request = RegistrationRequest.builder()
                    .firstname("John")
                    .lastname("Doe")
                    .email("john.doe@example.com")
                    .idno("34341322221")
                    .build();


            // Call the register method
            RegistrationResponse result = userService.register(request);

            // Verify userRepository methods were called
            verify(userRepository, times(1)).existsByIdno("34341322221");

            // Verify userRepository save method was not called
            verify(userRepository, never()).save(any());

            // Verify accountRepository method was not called
            verify(accountRepository, never()).save(any());

            // Assert the result message
            assertThat(result).toString().contains("User with ID Number already exists");
        }
    }