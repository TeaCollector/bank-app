package ru.alex.mscalc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.alex.mscalc.web.dto.EmploymentDto;
import ru.alex.mscalc.web.dto.LoanOfferDto;
import ru.alex.mscalc.web.dto.LoanStatementRequestDto;
import ru.alex.mscalc.web.dto.ScoringDataDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


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
        when(employmentService.calculateRateByEmployment(any(), any(), any()))
                .thenReturn(BigDecimal.valueOf(15.00));
    }

    @Test
    @DisplayName("When data is correct - success")
    void success() {
        var loanStatementRequest = getLoanStatementRequestDto();

        assertDoesNotThrow(() -> calculatorService.offer(loanStatementRequest));
    }

    @Test
    @DisplayName("Expected list size - success")
    void correctSize() {
        var loanStatementRequest = getLoanStatementRequestDto();
        var expected = 4;

        var result = calculatorService.offer(loanStatementRequest);

        assertEquals(expected, result.size());
    }

    @Test
    @DisplayName("Check valid rate in offer")
    void checkRate() {
        var loanStatementRequest = getLoanStatementRequestDto();
        var finalRate = List.of(BigDecimal.valueOf(15.00), BigDecimal.valueOf(13.00),
                BigDecimal.valueOf(12.00), BigDecimal.valueOf(10.00));

        List<BigDecimal> result = calculatorService.offer(loanStatementRequest)
                .stream()
                .map(LoanOfferDto::getRate)
                .toList();

        assertEquals(finalRate, result, "");
    }

    @ParameterizedTest
    @MethodSource("createValueForCheckMonthlyPayment")
    @DisplayName("Check monthly payment - success")
    void checkMonthlyPayment(Integer term, List<BigDecimal> expectedMonthlyPayment) {
        var loanStatementRequest = getLoanStatementRequestDto();
        loanStatementRequest.setTerm(term);

        var result = calculatorService.offer(loanStatementRequest)
                .stream()
                .map(LoanOfferDto::getMonthlyPayment)
                .toList();

        assertEquals(expectedMonthlyPayment, result);
    }

    static Stream<Arguments> createValueForCheckMonthlyPayment() {
        return Stream.of(
                Arguments.of(6, List.of(BigDecimal.valueOf(52210.14), BigDecimal.valueOf(51912.87), BigDecimal.valueOf(63842.91), BigDecimal.valueOf(63477.72))),
                Arguments.of(9, List.of(BigDecimal.valueOf(35451.18), BigDecimal.valueOf(35164.83), BigDecimal.valueOf(58953.9).setScale(2), BigDecimal.valueOf(58474.96))),
                Arguments.of(12, List.of(BigDecimal.valueOf(27077.49), BigDecimal.valueOf(26795.18), BigDecimal.valueOf(56863.23), BigDecimal.valueOf(56266.17))));

    }

    private LoanStatementRequestDto getLoanStatementRequestDto() {
        return LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(300000))
                .term(6)
                .firstName("Alexandr")
                .lastName("Sergeev")
                .middleName("Yurievich")
                .email("sasha@mail.com")
                .birthdate(LocalDate.of(1995, 12, 31))
                .passportSeries("4563")
                .passportNumber("305812")
                .build();
    }
}