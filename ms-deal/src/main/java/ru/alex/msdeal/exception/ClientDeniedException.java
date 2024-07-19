package ru.alex.msdeal.exception;

public class ClientDeniedException extends RuntimeException {
    public ClientDeniedException(String msg) {
        super(msg);
    }
}
