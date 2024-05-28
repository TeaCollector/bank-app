package ru.alex.mscalc.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.alex.mscalc.web.dto.EmploymentDto;
import ru.alex.mscalc.web.dto.LoanStatementRequestDto;
import ru.alex.mscalc.web.dto.ScoringDataDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;


@ExtendWith({SpringExtension.class, MockitoExtension.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = CalculatorService.class)
class CalculatorServiceTest {

    @MockBean
    ClientService clientService;
    @MockBean
    EmploymentService employmentService;
    @Autowired
    CalculatorService calculatorService;

    @BeforeEach
     void setup() {
        doNothing().when(clientService).validateData(any(ScoringDataDto.class));
        when(employmentService.calculateRateByEmployment(any(EmploymentDto.class), any(), any()))
            .thenReturn(BigDecimal.valueOf(15.00));
    }

    @Test
    @DisplayName("When data is correct - success")
    void success() {
        var loanStatementRequest = getLoanStatementRequestDto();

        assertDoesNotThrow(() -> calculatorService.offer(loanStatementRequest));
    }

    @Test
    @DisplayName("Check on correct list size")
    void correctSize() {
        var loanStatementRequest = getLoanStatementRequestDto();
        var expected = 4;

        var result = calculatorService.offer(loanStatementRequest);

        assertEquals(expected, result.size());
    }

    private LoanStatementRequestDto getLoanStatementRequestDto() {
        return LoanStatementRequestDto.builder()
            .amount(BigDecimal.valueOf(300000))
            .term(6)
            .firstName("Alexandr")
            .lastName("Sergeev")
            .middleName("Yurievich")
            .email("sasha@mail.com")
            .birthdate(LocalDate.of(1995, 12,31))
            .passportSeries("4563")
            .passportNumber("305812")
            .build();
    }
}