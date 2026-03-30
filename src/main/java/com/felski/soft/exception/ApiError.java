package com.felski.soft.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ApiError(
        int status,
        String message,
        LocalDateTime timestamp,
        List<String> fieldErrors
) {
    public ApiError(int status, String message) {
        this(status, message, LocalDateTime.now(), List.of());
    }
}
