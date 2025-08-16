package ru.don_polesie.back_end.exceptions;

public class RequestValidationException extends RuntimeException{
    public RequestValidationException(String message) {
        super(message);
    }
}
