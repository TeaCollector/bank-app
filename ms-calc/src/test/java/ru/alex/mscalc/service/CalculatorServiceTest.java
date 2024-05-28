package ru.alex.mscalc.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = CalculatorService.class)
class CalculatorServiceTest {

    @MockBean
    ClientService clientService;
    @MockBean
    EmploymentService employmentService;

    @InjectMocks
    @Autowired
    CalculatorService calculatorService;

    @Test
    @DisplayName("When data is correct success")
    void success() {



    }





}