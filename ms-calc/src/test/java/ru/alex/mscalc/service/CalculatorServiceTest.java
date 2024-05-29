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
import ru.alex.mscalc.entity.constant.EmploymentStatus;
import ru.alex.mscalc.entity.constant.Gender;
import ru.alex.mscalc.entity.constant.MaritalStatus;
import ru.alex.mscalc.entity.constant.Position;
import ru.alex.mscalc.web.dto.EmploymentDto;
import ru.alex.mscalc.web.dto.LoanOfferDto;
import ru.alex.mscalc.web.dto.LoanStatementRequestDto;
import ru.alex.mscalc.web.dto.ScoringDataDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        var finalRate = List.of(BigDecimal.valueOf(15.00).setScale(2), BigDecimal.valueOf(13.00).setScale(2),
                BigDecimal.valueOf(12.00).setScale(2), BigDecimal.valueOf(10.00).setScale(2));

        var result = calculatorService.offer(loanStatementRequest)
                .stream()
                .map(loanOfferDto -> loanOfferDto.getRate().setScale(2))
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
                .map(loanOfferDto -> loanOfferDto.getMonthlyPayment().setScale(2))
                .toList();

        assertEquals(expectedMonthlyPayment, result);
    }

    @Test
    @DisplayName("Method execution")
    void check() {
        var scoringDataDto = getScoringDataDto();

        assertDoesNotThrow(() -> calculatorService.scoreData(scoringDataDto));
    }

    @Test
    @DisplayName("When data is valid payment schedule size equals term")
    void checkEqualsEmploymentSchedule() {
        var scoringDataDto = getScoringDataDto();
        var expect = scoringDataDto.getTerm();

        var result = calculatorService.scoreData(scoringDataDto).getPaymentSchedule().size();

        assertEquals(expect, result);
    }

    @Test
    @DisplayName("When data is valid monthly payment correct")
    void checkValidMonthlyPayment() {
        var scoringDataDto = getScoringDataDto();
        var expect = BigDecimal.valueOf(310587.06);

        var result = calculatorService.scoreData(scoringDataDto).getPsk();

        assertEquals(expect, result);
    }

    @Test
    @DisplayName("When data is valid rate is correct")
    void checkFinalRate() {
        var scoringDataDto = getScoringDataDto();
        var expect = BigDecimal.valueOf(11.00).setScale(2);

        var result = calculatorService.scoreData(scoringDataDto).getRate();

//        assertEquals(expect, result);
    }

//    @Test
//    @DisplayName("When data is valid rate is correct")
//    void checkFinalRate() {
//        var scoringDataDto = getScoringDataDto().getEmployment();
//        scoringDataDto.setEmploymentStatus(EmploymentStatus.UNEMPLOYED);
//
//        assertEquals(expect, result);
//    }

    private ScoringDataDto getScoringDataDto() {
        return ScoringDataDto.builder()
            .amount(BigDecimal.valueOf(300000))
            .term(6)
            .firstName("Alexandr")
            .lastName("Sergeev")
            .middleName("Yurievich")
            .gender(Gender.MALE)
            .birthdate(LocalDate.of(1992, 7, 31))
            .passportSeries("4563")
            .passportNumber("305812")
            .passportIssueDate(LocalDate.of(2010, 2,19))
            .passportIssueBranch("OUFMS Russia")
            .dependentAmount(3000)
            .employment(EmploymentDto.builder()
                .employmentStatus(EmploymentStatus.EMPLOYEE)
                .employerINN("496185400491")
                .salary(BigDecimal.valueOf(95_000))
                .workExperienceTotal(60)
                .workExperienceCurrent(18)
                .position(Position.TOP_MANAGER)
                .build())
            .account("559293560734")
            .isInsuranceEnabled(true)
            .isSalaryClient(false)
            .maritalStatus(MaritalStatus.MARRIED)
            .build();
    }


    static Stream<Arguments> createValueForCheckMonthlyPayment() {
        return Stream.of(
                Arguments.of(6, List.of(BigDecimal.valueOf(52210.14), BigDecimal.valueOf(51912.87), BigDecimal.valueOf(52929.22), BigDecimal.valueOf(52626.46))),
                Arguments.of(9, List.of(BigDecimal.valueOf(35451.18), BigDecimal.valueOf(35164.83), BigDecimal.valueOf(35810.12), BigDecimal.valueOf(35519.20).setScale(2))),
                Arguments.of(12, List.of(BigDecimal.valueOf(27077.49), BigDecimal.valueOf(26795.18), BigDecimal.valueOf(27254.37), BigDecimal.valueOf(26968.20).setScale(2))));

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