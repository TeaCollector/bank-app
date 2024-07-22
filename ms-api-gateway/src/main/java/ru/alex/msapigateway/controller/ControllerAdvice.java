package ru.alex.msapigateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.alex.msapigateway.dto.MessageError;


@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @SneakyThrows
    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageError feignException(final FeignException e) {
        var objectMapper = new ObjectMapper();
        var errorMessage = objectMapper.readValue(e.contentUTF8(), MessageError.class);
        log.error("Error description: {}", e.contentUTF8());
        return new MessageError("Sorry, your loan was refused.", errorMessage.getErrors());
    }

    @SneakyThrows
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageError otherException(final RuntimeException e) {
        log.error("Error description: ", e);
        return new MessageError(e.getMessage());
    }
}
