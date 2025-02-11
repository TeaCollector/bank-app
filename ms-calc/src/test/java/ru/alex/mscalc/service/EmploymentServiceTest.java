package ru.alex.mscalc.service;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.alex.mscalc.entity.constant.EmploymentStatus;
import ru.alex.mscalc.entity.constant.Position;
import ru.alex.mscalc.exception.CurrentWorkExperienceException;
import ru.alex.mscalc.exception.TooLittleSalaryException;
import ru.alex.mscalc.exception.TotalWorkExperienceException;
import ru.alex.mscalc.exception.UnemployedException;
import ru.alex.mscalc.dto.EmploymentDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = EmploymentService.class)
class EmploymentServiceTest {

    @Value("${app.main-rate}")
    BigDecimal mainRate;

    @Autowired
    EmploymentService employmentService;

    private EmploymentDto employmentDto;
    private BigDecimal amount;

    @BeforeEach
    void setup() {
        employmentDto = getEmploymentDtoTemplate();
        amount = BigDecimal.valueOf(300_000);
    }

    @Test
    @DisplayName("Correct result on calculate rate by Employment")
    void correctResult() {
        var amount = BigDecimal.valueOf(300_000);
        var expected = BigDecimal.valueOf(20.00);

        var actual = employmentService.calculateRateByEmployment(employmentDto, amount, mainRate);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("When client position is CEO correct result")
    void clientCEO() {
        var expected = BigDecimal.valueOf(22.50);
        employmentDto.setPosition(Position.CEO);

        var actual = employmentService.calculateRateByEmployment(employmentDto, amount, mainRate);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("When client position is CFO correct result")
    void clientCFO() {
        var expected = BigDecimal.valueOf(21.00);
        employmentDto.setPosition(Position.CFO);

        var actual = employmentService.calculateRateByEmployment(employmentDto, amount, mainRate);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("When client position is ADMINISTRATOR correct result")
    void clientAdministrator() {
        var expected = BigDecimal.valueOf(18.50);
        employmentDto.setPosition(Position.ADMINISTRATOR);

        var actual = employmentService.calculateRateByEmployment(employmentDto, amount, mainRate);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("When client position is SIMPLE_MANAGER correct result")
    void clientSimpleManager() {
        var expected = BigDecimal.valueOf(18.00);
        employmentDto.setPosition(Position.SIMPLE_MANAGER);

        var actual = employmentService.calculateRateByEmployment(employmentDto, amount, mainRate);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("When client status is WORKER correct result")
    void clientStatusWorker() {
        var expected = BigDecimal.valueOf(19.00);
        employmentDto.setEmploymentStatus(EmploymentStatus.WORKER);

        var actual = employmentService.calculateRateByEmployment(employmentDto, amount, mainRate);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("When client status is WORKER correct result")
    void clientStatusSelfEmployed() {
        var expected = BigDecimal.valueOf(18.50);
        employmentDto.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED);

        var actual = employmentService.calculateRateByEmployment(employmentDto, amount, mainRate);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("When status UNEMPLOYED throw: UnemployedException")
    void throwUnemployedException() {
        employmentDto.setEmploymentStatus(EmploymentStatus.UNEMPLOYED);

        assertThrows(UnemployedException.class,
            () -> employmentService.calculateRateByEmployment(employmentDto, amount, mainRate));
    }

    @Test
    @DisplayName("When total working experience less than 18 month throw: TotalWorkExperienceException")
    void throwTotalWorkExperienceException() {
        employmentDto.setWorkExperienceTotal(17);

        assertThrows(TotalWorkExperienceException.class,
            () -> employmentService.calculateRateByEmployment(employmentDto, amount, mainRate));
    }

    @Test
    @DisplayName("When current working experience less than 3 month throw: CurrentWorkExperienceException")
    void throwCurrentWorkExperienceException() {
        var amount = BigDecimal.valueOf(300_000);
        employmentDto.setWorkExperienceCurrent(2);

        assertThrows(CurrentWorkExperienceException.class,
            () -> employmentService.calculateRateByEmployment(employmentDto, amount, mainRate));
    }

    @Test
    @DisplayName("When salary too little throw: TooLittleSalaryException")
    void throwTooLittleSalaryException() {
        var amount = BigDecimal.valueOf(300_000);
        employmentDto.setSalary(BigDecimal.valueOf(10_000));

        assertThrows(TooLittleSalaryException.class,
            () -> employmentService.calculateRateByEmployment(employmentDto, amount, mainRate));
    }


    private EmploymentDto getEmploymentDtoTemplate() {
        return EmploymentDto.builder()
            .employerINN("456234523005")
            .employmentStatus(EmploymentStatus.EMPLOYED)
            .position(Position.TOP_MANAGER)
            .workExperienceCurrent(20)
            .workExperienceTotal(56)
            .salary(BigDecimal.valueOf(95_000))
            .build();
    }
}