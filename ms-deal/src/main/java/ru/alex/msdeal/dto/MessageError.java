package ru.alex.msdeal.dto;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class MessageError {
    private String message;
    private Map<String, String> errors;

    public MessageError(final String message) {
        this.message = message;
        this.errors = new HashMap<>();
    }
}