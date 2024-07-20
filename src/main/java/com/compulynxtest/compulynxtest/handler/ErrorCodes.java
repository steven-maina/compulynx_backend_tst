package com.compulynxtest.compulynxtest.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "The new password does not match"),
    ACCOUNT_LOCKED(302, FORBIDDEN, "Customer account is locked"),
    ACCOUNT_DISABLED(303, FORBIDDEN, "Customer account is disabled"),
    BAD_CREDENTIALS(401, FORBIDDEN, "Incorrect Credentials"),
    ACCOUNT_NOT_FOUND(404, NOT_FOUND, "Account Not Found"),
    ACCOUNT_NOT_ACTIVE(405, FORBIDDEN, "Account Not Active"),
    INVALID_TOKEN(406, FORBIDDEN, "Invalid Token"),
    INVALID_REQUEST(407, BAD_REQUEST, "Invalid Request"),
    INVALID_INPUT(408, BAD_REQUEST, "Invalid Input"),
    INPUT_CONFLICT(409, CONFLICT, "Input Conflict"),
    DUPLICATE_ENTRY(410, CONFLICT, "Duplicate Entry"),
    OPERATION_NOT_PERMITTED(411, FORBIDDEN, "Operation Not Permitted"),
    UNEXPECTED_ERROR(500, INTERNAL_SERVER_ERROR, "Unexpected Error");
    ;

    private final int code;
    private final String description;
    private final HttpStatus httpStatus;
    ErrorCodes(int code, HttpStatus status, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = status;
    }
}
