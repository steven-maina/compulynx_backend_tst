package com.compulynxtest.compulynxtest.exception;

import lombok.Getter;

@Getter
public class DuplicateEntryException extends RuntimeException {

    private final String fieldName;
    private final String errorMessage;

    public DuplicateEntryException(String fieldName, String errorMessage) {
        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
    }

}
