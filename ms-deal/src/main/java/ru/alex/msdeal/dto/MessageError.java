package ru.alex.msdeal.dto;

import java.util.HashMap;
import java.util.Map;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageError {
    private String message;
    private Map<String, String> errors;

    public MessageError(final String message) {
        this.message = message;
        this.errors = new HashMap<>();
    }
}