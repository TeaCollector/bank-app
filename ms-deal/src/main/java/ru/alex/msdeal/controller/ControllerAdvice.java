package ru.alex.msdeal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.alex.msdeal.dto.MessageError;
import ru.alex.msdeal.exception.StatementNotPreApprovedException;


@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(StatementNotPreApprovedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageError validation(final RuntimeException e) {
        log.error("Error description: {}", e.getStackTrace());
        return new MessageError("Sorry, your loan was refused: " + e.getMessage());
    }

    @SneakyThrows
    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageError feignException(final FeignException e) {
        var objectMapper = new ObjectMapper();
        var errorMessage = objectMapper.readValue(e.contentUTF8(), MessageError.class);
        log.error("Error description: {}", e.contentUTF8());
        return new MessageError("Sorry, your loan was refused.", errorMessage.getErrors());
    }
}
