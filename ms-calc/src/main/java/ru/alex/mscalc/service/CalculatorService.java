package ru.alex.mscalc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.alex.mscalc.exception.OldAgeException;
import ru.alex.mscalc.exception.YoungAgeException;
import ru.alex.mscalc.util.RateComparator;
import ru.alex.mscalc.web.dto.CreditDto;
import ru.alex.mscalc.web.dto.LoanOfferDto;
import ru.alex.mscalc.web.dto.LoanStatementRequestDto;
import ru.alex.mscalc.web.dto.ScoringDataDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CalculatorService {

    @Value("${app.main-rate}")
    private BigDecimal mainRate;
    @Value("${app.min-age}")
    private Integer minAge;
    @Value("${app.max-age}")
    private Integer maxAge;

    private final ClientService clientService;
    private final EmploymentService employmentService;

    public List<LoanOfferDto> offer(LoanStatementRequestDto loanStatementRequestDto) {
        var isInsuranceAdd = true;
        var isSalaryClient = clientService.findClient(loanStatementRequestDto.getEmail());
        var insurance = calculateInsurance(loanStatementRequestDto.getAmount(), loanStatementRequestDto.getTerm());

        List<LoanOfferDto> loanOfferDtoList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            BigDecimal totalAmount;
            BigDecimal rate;

            UUID statmentUUID = UUID.randomUUID();
            LoanOfferDto loanOfferDto = LoanOfferDto.builder()
                    .statementId(statmentUUID)
                    .requestAmount(loanStatementRequestDto.getAmount())
                    .term(loanStatementRequestDto.getTerm())
                    .build();

            if (isSalaryClient) {
                loanOfferDto.setIsSalaryClient(true);
                if (isInsuranceAdd) {
                    loanOfferDto.setIsInsuranceEnabled(true);
                    rate = calculateRate(isSalaryClient, isInsuranceAdd, mainRate);
                    loanOfferDto.setRate(rate);
                    totalAmount = loanStatementRequestDto.getAmount().add(insurance);
                    isInsuranceAdd = false;
                } else {
                    loanOfferDto.setIsInsuranceEnabled(false);
                    rate = calculateRate(isSalaryClient, isInsuranceAdd, mainRate);
                    loanOfferDto.setRate(rate);
                    totalAmount = loanStatementRequestDto.getAmount();
                    isSalaryClient = false;
                    isInsuranceAdd = true;
                }
            } else {
                loanOfferDto.setIsSalaryClient(false);
                if (isInsuranceAdd) {
                    loanOfferDto.setIsInsuranceEnabled(true);
                    rate = calculateRate(isSalaryClient, isInsuranceAdd, mainRate);
                    loanOfferDto.setRate(rate);
                    totalAmount = loanStatementRequestDto.getAmount().add(insurance);
                    isInsuranceAdd = false;
                } else {
                    loanOfferDto.setIsInsuranceEnabled(false);
                    rate = calculateRate(isSalaryClient, isInsuranceAdd, mainRate);
                    loanOfferDto.setRate(rate);
                    totalAmount = loanStatementRequestDto.getAmount();
                }
            }
            loanOfferDto.setTotalAmount(totalAmount);
            loanOfferDto.setMonthlyPayment(calculateMonthlyPayment(loanStatementRequestDto.getTerm(), totalAmount, rate));
            loanOfferDtoList.add(loanOfferDto);
        }
        loanOfferDtoList.sort(new RateComparator());
        return loanOfferDtoList;
    }

    public CreditDto scoreData(ScoringDataDto scoringDataDto) {
        BigDecimal rate = employmentService.scoreByEmployment(scoringDataDto.getEmployment(), scoringDataDto.getAmount());
        long yearOfClient = ChronoUnit.YEARS.between(scoringDataDto.getBirthDate(), LocalDate.now());
        if (yearOfClient < minAge) {
            throw new YoungAgeException("Sorry you too young");
        } else if (yearOfClient > maxAge) {
            throw new OldAgeException("Sorry you too old");
        }

        switch (scoringDataDto.getGender()) {
            case MALE -> {
                if (yearOfClient >= 30 && yearOfClient <= 55) {
                    rate = rate.subtract(BigDecimal.valueOf(3.00d));
                } else {
                    rate = rate.subtract(BigDecimal.valueOf(1.75d));
                }
            }
            case FEMALE -> {
                if (yearOfClient >= 32 && yearOfClient <= 60) {
                    rate = rate.subtract(BigDecimal.valueOf(3.00d));
                } else {
                    rate = rate.subtract(BigDecimal.valueOf(2.00d));
                }
            }
            case TRANSGENDER -> rate = rate.add(BigDecimal.valueOf(8.65d));
        }

        switch (scoringDataDto.getMaritalStatus()) {
            case SINGLE -> rate = rate.add(BigDecimal.valueOf(1.00d));
            case MARRIED -> rate = rate.subtract(BigDecimal.valueOf(3.00d));
            case WIDOWED -> rate = rate.subtract(BigDecimal.valueOf(2.00d));
        }

        rate = calculateRate(scoringDataDto.getIsSalaryClient(), scoringDataDto.getIsInsuranceEnabled(), rate);



        return null;
    }

    private BigDecimal calculateRate(boolean isSalaryClient, boolean isInsuranceAdd, BigDecimal rate) {
        if (isSalaryClient) {
            rate = mainRate.subtract(BigDecimal.valueOf(2.00d)).setScale(2, RoundingMode.HALF_UP);
        } else if (isInsuranceAdd) {
            rate = mainRate.subtract(BigDecimal.valueOf(3.00d)).setScale(2, RoundingMode.HALF_UP);
        }
        return rate;
    }

    private BigDecimal calculateMonthlyPayment(Integer term, BigDecimal totalAmount, BigDecimal rate) {
        double monthlyRate = rate.doubleValue() / 100 / 12;
        double monthlyRateInTermPow = Math.pow(1 + monthlyRate, term);
        double monthlyPayment = monthlyRate * monthlyRateInTermPow / (monthlyRateInTermPow - 1);
        return new BigDecimal(monthlyPayment).multiply(totalAmount).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateInsurance(BigDecimal amount, Integer term) {
        return amount.multiply(BigDecimal.valueOf(term))
                .multiply(BigDecimal.valueOf(mainRate.doubleValue() / 100))
                .setScale(2, RoundingMode.HALF_UP)
                .subtract(BigDecimal.valueOf(100000));
    }
}
