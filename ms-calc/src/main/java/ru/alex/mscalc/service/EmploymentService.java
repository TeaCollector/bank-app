package ru.alex.mscalc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.alex.mscalc.exception.CurrentWorkExperienceException;
import ru.alex.mscalc.exception.TooLittleSalaryException;
import ru.alex.mscalc.exception.TotalWorkExperienceException;
import ru.alex.mscalc.exception.UnemployedException;
import ru.alex.mscalc.repository.EmploymentRepository;
import ru.alex.mscalc.web.dto.EmploymentDto;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class EmploymentService {

    @Value("${app.main-rate}")
    private BigDecimal mainRate;
    private static final Integer SALARY_MIN = 25;

    private final EmploymentRepository employmentRepository;


    public BigDecimal scoreByEmployment(EmploymentDto employmentDto, BigDecimal amount) {
        BigDecimal rate = mainRate;

        employmentRepository.findByInn(employmentDto.getEmployerINN());

        switch (employmentDto.getEmploymentStatus()) {
            case WORKER -> rate = rate.add(BigDecimal.valueOf(1.00d));
            case EMPLOYEE -> rate = rate.add(BigDecimal.valueOf(2.00d));
            case BUSINESS_OWNER -> rate = rate.add(BigDecimal.valueOf(3.00d));
            case SELF_EMPLOYED -> rate = rate.add(BigDecimal.valueOf(0.50d));
            case UNEMPLOYED -> throw new UnemployedException("Sorry we can't give loan to unemployed");
        }

        switch (employmentDto.getPosition()) {
            case SIMPLE_MANAGER -> rate = rate.add(BigDecimal.valueOf(1.00d));
            case ADMINISTRATOR -> rate = rate.add(BigDecimal.valueOf(1.50d));
            case TOP_MANAGER -> rate = rate.add(BigDecimal.valueOf(3.00d));
            case CFO -> rate = rate.add(BigDecimal.valueOf(4.00d));
            case CEO -> rate = rate.add(BigDecimal.valueOf(5.50d));
        }

        if (employmentDto.getWorkExperienceTotal() < 18) {
            throw new TotalWorkExperienceException("Sorry your total experience less then 18 month");
        }

        if (employmentDto.getWorkExperienceCurrent() < 3) {
            throw new CurrentWorkExperienceException("Sorry, your current work experience less then 3 month");
        }

        if (amount.doubleValue() > employmentDto.getSalary().multiply(BigDecimal.valueOf(SALARY_MIN)).doubleValue()) {
            throw new TooLittleSalaryException("Sorry you have to little salary for this amount =)");
        }
        return rate;
    }
}
