package ru.alex.mscalc.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.alex.mscalc.entity.constant.EmploymentStatus;
import ru.alex.mscalc.entity.constant.Gender;
import ru.alex.mscalc.entity.constant.MaritalStatus;
import ru.alex.mscalc.entity.constant.Position;
import ru.alex.mscalc.web.dto.EmploymentDto;
import ru.alex.mscalc.web.dto.LoanStatementRequestDto;
import ru.alex.mscalc.web.dto.ScoringDataDto;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = { CalculatorService.class, EmploymentService.class, ClientService.class })
class CalculatorServiceTest {

    @Autowired
    CalculatorService calculatorService;

    private ScoringDataDto scoringDataDto;
    private LoanStatementRequestDto loanStatementRequestDto;

    @BeforeEach
    void setup() {
        scoringDataDto = getScoringDataDto();
        loanStatementRequestDto = getLoanStatementRequestDto();
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
        var expected = 4;

        var result = calculatorService.offer(loanStatementRequestDto);

        assertEquals(expected, result.size());
    }

    @Test
    @DisplayName("Check valid rate in offer")
    void checkRate() {
        var finalRate = List.of(BigDecimal.valueOf(15.00).setScale(2), BigDecimal.valueOf(13.00).setScale(2),
            BigDecimal.valueOf(12.00).setScale(2), BigDecimal.valueOf(10.00).setScale(2));

        var result = calculatorService.offer(loanStatementRequestDto)
            .stream()
            .map(loanOfferDto -> loanOfferDto.getRate().setScale(2))
            .toList();

        assertEquals(finalRate, result, "");
    }

    @ParameterizedTest
    @MethodSource("createValueForCheckMonthlyPayment")
    @DisplayName("Check monthly payment - success")
    void checkMonthlyPayment(Integer term, List<BigDecimal> expectedMonthlyPayment) {
        loanStatementRequestDto.setTerm(term);

        var result = calculatorService.offer(loanStatementRequestDto)
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
        var expect = scoringDataDto.getTerm();

        var result = calculatorService.scoreData(scoringDataDto).getPaymentSchedule().size();

        assertEquals(expect, result);
    }

    @Test
    @DisplayName("When data is valid monthly payment correct")
    void checkValidMonthlyPayment() {
        var expect = BigDecimal.valueOf(52_777.72);

        var result = calculatorService.scoreData(scoringDataDto).getMonthlyPayment();

        assertEquals(expect, result);
    }

    @Test
    @DisplayName("When data is valid rate is correct")
    void checkFinalRate() {
        var expect = BigDecimal.valueOf(11.00).setScale(2);

        var result = calculatorService.scoreData(scoringDataDto).getRate();

        assertEquals(expect, result);
    }

    @Test
    @DisplayName("Correct rate when client marital status WIDOWED")
    void checkRateByMaritalStatusWidowed() {
        scoringDataDto.setMaritalStatus(MaritalStatus.WIDOWED);
        var expect = BigDecimal.valueOf(12.00).setScale(2);

        var result = calculatorService.scoreData(scoringDataDto).getRate();

        assertEquals(expect, result);
    }

    @Test
    @DisplayName("Correct rate when client marital status SINGLE")
    void checkRateByMaritalStatusSingle() {
        scoringDataDto.setMaritalStatus(MaritalStatus.SINGLE);
        var expect = BigDecimal.valueOf(15.00).setScale(2);

        var result = calculatorService.scoreData(scoringDataDto).getRate();

        assertEquals(expect, result);
    }

    @Test
    @DisplayName("Correct rate when client gender TRANSGENDER")
    void checkRateByGenderTransgender() {
        scoringDataDto.setGender(Gender.TRANSGENDER);
        var expect = BigDecimal.valueOf(22.65).setScale(2);

        var result = calculatorService.scoreData(scoringDataDto).getRate();

        assertEquals(expect, result);
    }

    @Test
    @DisplayName("Correct rate when client gender FEMALE")
    void checkRateByGenderFemale() {
        scoringDataDto.setGender(Gender.FEMALE);
        var expect = BigDecimal.valueOf(12.00).setScale(2);

        var result = calculatorService.scoreData(scoringDataDto).getRate();

        assertEquals(expect, result);
    }

    @Test
    @DisplayName("Correct rate when client under 32 y.o.")
    void checkRateByBirthDate() {
        scoringDataDto.setBirthdate(LocalDate.of(2000, 2, 4));
        var expect = BigDecimal.valueOf(12.25).setScale(2);

        var result = calculatorService.scoreData(scoringDataDto).getRate();

        assertEquals(expect, result);
    }

    @Test
    @DisplayName("Correct rate when client gender FEMALE and age under 32 y.o.")
    void checkRateByGenderFemaleAndBirthdate() {
        scoringDataDto.setBirthdate(LocalDate.of(2000, 2, 4));
        scoringDataDto.setGender(Gender.FEMALE);
        var expect = BigDecimal.valueOf(12.00).setScale(2);

        var result = calculatorService.scoreData(scoringDataDto).getRate();

        assertEquals(expect, result);
    }

    @ParameterizedTest
    @DisplayName("Correct rate when client gender FEMALE and age between 32 and 60 y.o.")
    @CsvSource(value = {
        "11.00, 1990-02-04",
        "11.00, 1980-03-04",
        "11.00, 1970-02-26"
    }, delimiter = ',')
    void checkBetweenRateByGenderFemaleAndBirthdate(BigDecimal expect, LocalDate birthdate) {
        scoringDataDto.setBirthdate(birthdate);
        scoringDataDto.setGender(Gender.FEMALE);

        var result = calculatorService.scoreData(scoringDataDto).getRate();

        assertEquals(expect, result);
    }

    @ParameterizedTest
    @DisplayName("Correct rate when client gender MALE and age between 30 and 55 y.o.")
    @CsvSource(value = {
        "11.00, 1990-02-04",
        "11.00, 1980-03-04",
        "11.00, 1970-02-26"
    }, delimiter = ',')
    void checkRateByGenderMaleAndBirthdate(BigDecimal expect, LocalDate birthdate) {
        scoringDataDto.setBirthdate(birthdate);
        scoringDataDto.setGender(Gender.MALE);

        var result = calculatorService.scoreData(scoringDataDto).getRate();

        assertEquals(expect, result);
    }

    static Stream<Arguments> createValueForCheckMonthlyPayment() {
        return Stream.of(
            Arguments.of(6, List.of(BigDecimal.valueOf(52_210.14), BigDecimal.valueOf(51_912.87), BigDecimal.valueOf(52_929.22), BigDecimal.valueOf(52_626.46))),
            Arguments.of(9, List.of(BigDecimal.valueOf(35_451.18), BigDecimal.valueOf(35_164.83), BigDecimal.valueOf(35_810.12), BigDecimal.valueOf(35_519.20).setScale(2))),
            Arguments.of(12, List.of(BigDecimal.valueOf(27_077.49), BigDecimal.valueOf(26_795.18), BigDecimal.valueOf(27_254.37), BigDecimal.valueOf(26_968.20).setScale(2))));

    }

    private LoanStatementRequestDto getLoanStatementRequestDto() {
        return LoanStatementRequestDto.builder()
            .amount(BigDecimal.valueOf(300_000))
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

    private ScoringDataDto getScoringDataDto() {
        return ScoringDataDto.builder()
            .amount(BigDecimal.valueOf(300_000))
            .term(6)
            .firstName("Alexandr")
            .lastName("Sergeev")
            .middleName("Yurievich")
            .gender(Gender.MALE)
            .birthdate(LocalDate.of(1992, 7, 31))
            .passportSeries("4563")
            .passportNumber("305812")
            .passportIssueDate(LocalDate.of(2010, 2, 19))
            .passportIssueBranch("OUFMS Russia")
            .dependentAmount(3_000)
            .maritalStatus(MaritalStatus.MARRIED)
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
            .build();
    }
}