package ru.alex.msdeal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class StatementNotPreApprovedException extends RuntimeException {
    public StatementNotPreApprovedException(String msg) {
        super(msg);
    }
}
