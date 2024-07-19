package ru.alex.mscalc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class MessageError {
    private String message;
    private Map<String, String> errors;

    public MessageError(final String message) {
        this.message = message;
    }
}
