package com.compulynxtest.compulynxtest.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionResponse {

    private Integer errorCode;
    private String errorDescription;
    private String error;
    private Set<String> validationErrors;
    private Map<String, String> errors;

    public ExceptionResponse(String error, String errorMessage) {
        this.error = error;
        this.errorDescription = errorMessage;
    }
}
