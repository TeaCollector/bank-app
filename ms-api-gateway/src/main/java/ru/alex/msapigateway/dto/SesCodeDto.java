package ru.alex.msapigateway.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class SesCodeDto {
    @NotNull
    private int sesCode;
}
