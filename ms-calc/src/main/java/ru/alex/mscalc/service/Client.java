package ru.alex.mscalc.service;

import java.time.LocalDate;

public record Client(String email,
                     String firstName,
                     String lastName,
                     String middleName,
                     LocalDate birthDate,
                     String passportSeries,
                     String passportNumber) {
}
