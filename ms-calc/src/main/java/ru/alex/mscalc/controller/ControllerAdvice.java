package ru.alex.mscalc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.alex.mscalc.dto.MessageError;
import ru.alex.mscalc.exception.*;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageError validation(final MethodArgumentNotValidException e) {
        log.error("Error was captured: {}", e.getMessage());
        Map<String, String> errors = e.getBindingResult()
                .getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage,
                        (existingMessage, newMessage) ->
                                existingMessage + " " + newMessage));
        return new MessageError("Validation failed.", errors);
    }

    @ExceptionHandler({
            CurrentWorkExperienceException.class,
            OldAgeException.class,
            TooLittleSalaryException.class,
            TotalWorkExperienceException.class,
            UnemployedException.class,
            YoungAgeException.class,
            InvalidPassportIssuesException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageError badRequest(final RuntimeException e) {
        log.error("Error description: {}", e.getStackTrace());
        return new MessageError("Sorry, you were refused a loan");
    }
}
