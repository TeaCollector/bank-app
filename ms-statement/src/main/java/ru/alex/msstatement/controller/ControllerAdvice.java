package ru.alex.msstatement.controller;

import java.util.Map;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.alex.msstatement.dto.MessageError;


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

    @SneakyThrows
    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageError feignException(final FeignException e) {
        var objectMapper = new ObjectMapper();
        var errorMessage = objectMapper.readValue(e.contentUTF8(), MessageError.class);
        log.error("Error description: {}", e.contentUTF8());
        return new MessageError("Validation failed.", errorMessage.getErrors());
    }

}
