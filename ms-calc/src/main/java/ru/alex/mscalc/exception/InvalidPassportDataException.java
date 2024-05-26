package ru.alex.mscalc.exception;

public class InvalidPassportDataException extends RuntimeException {
    public InvalidPassportDataException(String message) {
        super(message);
    }
}
