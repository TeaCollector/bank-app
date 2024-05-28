package ru.alex.mscalc.web.api;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.alex.mscalc.exception.*;
import ru.alex.mscalc.web.dto.MessageError;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageError validation(final MethodArgumentNotValidException e) {
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
    public MessageError badRequest(final MethodArgumentNotValidException e) {
        Map<String, String> errors = e.getBindingResult()
                .getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage,
                        (existingMessage, newMessage) ->
                                existingMessage + " " + newMessage));
        return new MessageError("Validation failed.", errors);
    }
}
