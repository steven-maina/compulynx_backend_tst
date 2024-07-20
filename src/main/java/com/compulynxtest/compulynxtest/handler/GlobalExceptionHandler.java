package com.compulynxtest.compulynxtest.handler;

import com.compulynxtest.compulynxtest.exception.ActivationTokenException;
import com.compulynxtest.compulynxtest.exception.DuplicateEntryException;
import com.compulynxtest.compulynxtest.exception.NoTransactionsFoundException;
import com.compulynxtest.compulynxtest.exception.OperationNotPermittedException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.compulynxtest.compulynxtest.handler.ErrorCodes.*;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleException(LockedException exp) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .errorCode(ACCOUNT_LOCKED.getCode())
                                .errorDescription(ACCOUNT_LOCKED.getDescription())
                                .error(exp.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleException(DisabledException exp) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .errorCode(ACCOUNT_DISABLED.getCode())
                                .errorDescription(ACCOUNT_DISABLED.getDescription())
                                .error(exp.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(DuplicateEntryException.class)
    public ResponseEntity<ExceptionResponse> handleException(DuplicateEntryException exp) {
        return ResponseEntity
                .status(CONFLICT)
                .body(
                        ExceptionResponse.builder()
                                .errorCode(DUPLICATE_ENTRY.getCode())
                                .errorDescription(DUPLICATE_ENTRY.getDescription())
                                .error(exp.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(NoTransactionsFoundException.class)
    public final ResponseEntity<String> handleNoTransactionsFoundException(NoTransactionsFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), OK);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException() {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .errorCode(BAD_CREDENTIALS.getCode())
                                .errorDescription(BAD_CREDENTIALS.getDescription())
                                .build()
                );
    }

    @ExceptionHandler(ActivationTokenException.class)
    public ResponseEntity<ExceptionResponse> handleException(ActivationTokenException exp) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .error(exp.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(OperationNotPermittedException.class)
    public ResponseEntity<ExceptionResponse> handleException(OperationNotPermittedException exp) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .error(exp.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateEntryException(DuplicateKeyException ex) {
        return ResponseEntity
                .status(CONFLICT)
                .body( ExceptionResponse.builder()
                        .error(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
        Set<String> errors = new HashSet<>();
        exp.getBindingResult().getAllErrors()
                .forEach(error -> {
                    //var fieldName = ((FieldError) error).getField();
                    var errorMessage = error.getDefaultMessage();
                    errors.add(errorMessage);
                });

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .validationErrors(errors)
                                .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exp) {
        exp.printStackTrace();
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .errorDescription("Internal error, please contact the admin")
                                .error(exp.getMessage())
                                .build()
                );
    }
}
