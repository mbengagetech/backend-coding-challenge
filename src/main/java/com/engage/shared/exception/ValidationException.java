package com.engage.shared.exception;

import io.vavr.collection.Seq;

import java.util.List;

public class ValidationException extends RuntimeException {

    private List<String> errors;

    public ValidationException(Seq<String> errors) {
        this.errors = errors.asJava();
    }

    public List<String> errors() {
        return errors;
    }
}
