package org.example.graphql_davidroldan.domain.modelo.errores;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
