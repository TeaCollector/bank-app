package ru.alex.mscalc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.alex.mscalc.exception.CurrentWorkExperienceException;
import ru.alex.mscalc.exception.TooLittleSalaryException;
import ru.alex.mscalc.exception.TotalWorkExperienceException;
import ru.alex.mscalc.exception.UnemployedException;
import ru.alex.mscalc.web.dto.EmploymentDto;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmploymentService {

    private static final Integer SALARY_MIN = 15;

    public BigDecimal calculateRateByEmployment(EmploymentDto employmentDto, BigDecimal amount, BigDecimal mainRate) {
        log.info("Calculate rate by {}, amount: {}, main rate: {}", employmentDto, amount, mainRate);
        var rate = mainRate;
        switch (employmentDto.getEmploymentStatus()) {
            case WORKER -> rate = rate.add(BigDecimal.valueOf(1.00d));
            case EMPLOYEE -> rate = rate.add(BigDecimal.valueOf(2.00d));
            case SELF_EMPLOYED -> rate = rate.add(BigDecimal.valueOf(0.50d));
            case UNEMPLOYED -> {
                log.warn("Client is not working: reject");
                throw new UnemployedException("Sorry, we can't give loan to unemployed");
            }
        }
        log.info("Rate after employment status: {}", rate);

        switch (employmentDto.getPosition()) {
            case SIMPLE_MANAGER -> rate = rate.add(BigDecimal.valueOf(1.00d));
            case ADMINISTRATOR -> rate = rate.add(BigDecimal.valueOf(1.50d));
            case TOP_MANAGER -> rate = rate.add(BigDecimal.valueOf(3.00d));
            case CFO -> rate = rate.add(BigDecimal.valueOf(4.00d));
            case CEO -> rate = rate.add(BigDecimal.valueOf(5.50d));
        }

        if (employmentDto.getWorkExperienceTotal() < 18) {
            log.warn("Client total working experience less 18 month: reject");
            throw new TotalWorkExperienceException("Sorry, your total experience less 18 month");
        }

        if (employmentDto.getWorkExperienceCurrent() < 3) {
            log.warn("Client current working experience less 3 month: reject");
            throw new CurrentWorkExperienceException("Sorry, your current work experience less 3 month");
        }

        if (amount.doubleValue() > employmentDto.getSalary().multiply(BigDecimal.valueOf(SALARY_MIN)).doubleValue()) {
            log.warn("Client salary to little: reject");
            throw new TooLittleSalaryException("Sorry, you have to little salary for this amount");
        }
        log.info("Rate after employment position: {}", rate);
        return rate;
    }
}
