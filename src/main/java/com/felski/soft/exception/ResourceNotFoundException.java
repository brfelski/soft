package com.felski.soft.exception;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, UUID id) {
        super("%s não encontrado com id: %s".formatted(resource, id));
    }
}
