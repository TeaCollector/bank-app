package ru.alex.msdeal.exception;

public class StatementNotFoundException extends RuntimeException {
    public StatementNotFoundException(String msg) {
        super(msg);
    }
}
