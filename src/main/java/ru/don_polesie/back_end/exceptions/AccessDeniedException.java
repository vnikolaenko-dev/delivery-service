package ru.don_polesie.back_end.exceptions;

public class AccessDeniedException extends RuntimeException{

    public AccessDeniedException(String message) {
        super(message);
    }
}
