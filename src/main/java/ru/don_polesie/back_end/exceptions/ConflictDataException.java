package ru.don_polesie.back_end.exceptions;

public class ConflictDataException extends RuntimeException{
    public ConflictDataException(String message) {
        super(message);
    }
}
