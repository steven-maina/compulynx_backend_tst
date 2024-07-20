package com.compulynxtest.compulynxtest.exception;

public class NoTransactionsFoundException extends RuntimeException {
    public NoTransactionsFoundException(String message) {
        super(message);
    }
}
