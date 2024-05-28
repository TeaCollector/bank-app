package ru.alex.mscalc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.alex.mscalc.util.RateComparator;
import ru.alex.mscalc.web.dto.*;

import java.math.BigDecimal;
import java.math.MathContext;
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

    private BigDecimal totalInterestPayment;

    private final EmploymentService employmentService;
    private final ClientService clientService;

    public List<LoanOfferDto> offer(LoanStatementRequestDto loanStatementRequestDto) {
        var combination = List.of(true, true, false, false, true, false, false, true);
        var insurance = calculateInsurance(loanStatementRequestDto.getAmount(), loanStatementRequestDto.getTerm());
        var loanOfferDtoList = new ArrayList<LoanOfferDto>();

        for (int i = 0; i < combination.size() / 2; i++) {
            var totalAmount = BigDecimal.ZERO;
            var rate = BigDecimal.ZERO;

            var isSalaryClient = combination.get(i);
            var isInsuranceEnabled = combination.get(i + 1);
            rate = updateRateOnSalaryClientAndInsurance(isSalaryClient, isInsuranceEnabled, mainRate);
            if (isInsuranceEnabled) {
                totalAmount = loanStatementRequestDto.getAmount().add(insurance);
            } else {
                totalAmount = loanStatementRequestDto.getAmount();
            }

            LoanOfferDto loanOfferDto = LoanOfferDto.builder()
                    .statementId(UUID.randomUUID())
                    .requestAmount(loanStatementRequestDto.getAmount())
                    .term(loanStatementRequestDto.getTerm())
                    .rate(rate)
                    .isSalaryClient(isSalaryClient)
                    .isInsuranceEnabled(isInsuranceEnabled)
                    .totalAmount(totalAmount)
                    .monthlyPayment(calculateMonthlyPayment(loanStatementRequestDto.getTerm(), totalAmount, rate))
                    .build();

            loanOfferDtoList.add(loanOfferDto);
        }
        loanOfferDtoList.sort(new RateComparator());
        return loanOfferDtoList;
    }

    public CreditDto scoreData(ScoringDataDto scoringDataDto) {
        var amount = scoringDataDto.getAmount();
        clientService.validateData(scoringDataDto);
        var rate = employmentService.calculateRateByEmployment(scoringDataDto.getEmployment(), amount, mainRate);
        var term = scoringDataDto.getTerm();

        var yearOfClient = ChronoUnit.YEARS.between(scoringDataDto.getBirthdate(), LocalDate.now());

        rate = updateRateOnClientAge(scoringDataDto, yearOfClient, rate);
        rate = updateRateOnMaritalStatus(scoringDataDto, rate);
        rate = updateRateOnSalaryClientAndInsurance(scoringDataDto.getIsSalaryClient(), scoringDataDto.getIsInsuranceEnabled(), rate);
        if (scoringDataDto.getIsInsuranceEnabled()) {
            amount = amount.add(calculateInsurance(amount, scoringDataDto.getTerm()));
        }
        return CreditDto.builder()
                .isInsuranceEnabled(scoringDataDto.getIsInsuranceEnabled())
                .isSalaryClient(scoringDataDto.getIsSalaryClient())
                .term(scoringDataDto.getTerm())
                .rate(rate)
                .amount(amount)
                .monthlyPayment(calculateMonthlyPayment(scoringDataDto.getTerm(), amount, rate))
                .paymentSchedule(calculatePaymentSchedule(amount, term, rate))
                .psk(amount.add(totalInterestPayment).setScale(2, RoundingMode.HALF_UP))
                .build();
    }

    private BigDecimal updateRateOnMaritalStatus(ScoringDataDto scoringDataDto, BigDecimal rate) {
        switch (scoringDataDto.getMaritalStatus()) {
            case SINGLE -> rate = rate.add(BigDecimal.valueOf(1.00d));
            case MARRIED -> rate = rate.subtract(BigDecimal.valueOf(3.00d));
            case WIDOWED -> rate = rate.subtract(BigDecimal.valueOf(2.00d));
        }
        return rate;
    }

    private BigDecimal updateRateOnClientAge(ScoringDataDto scoringDataDto, long yearOfClient, BigDecimal rate) {
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
        return rate;
    }

    private List<PaymentScheduleElementDto> calculatePaymentSchedule(BigDecimal amount, Integer term, BigDecimal rate) {
        var totalAmount = amount;
        var monthlyRate = getMonthlyRate(rate);
        var paymentsList = new ArrayList<PaymentScheduleElementDto>();
        var monthlyPayment = calculateMonthlyPayment(term, amount, rate);
        totalInterestPayment = BigDecimal.ZERO;
        for (int i = 1; i <= term; i++) {
            var dateToPay = LocalDate.now().plusMonths(i);
            var interestPayment = amount.multiply(monthlyRate);
            var debtPayment = monthlyPayment.subtract(interestPayment.setScale(2, RoundingMode.HALF_EVEN));
            amount = amount.subtract(debtPayment);

            var paymentScheduleElementDto = PaymentScheduleElementDto.builder()
                    .number(i)
                    .date(dateToPay)
                    .totalAmount(totalAmount)
                    .interestPayment(interestPayment.setScale(2, RoundingMode.HALF_EVEN))
                    .debtPayment(debtPayment.setScale(2, RoundingMode.HALF_EVEN))
                    .remainingDebt(amount.setScale(2, RoundingMode.HALF_EVEN))
                    .build();

            totalInterestPayment = totalInterestPayment.add(interestPayment);

            paymentsList.add(paymentScheduleElementDto);
        }
        return paymentsList;
    }

    private BigDecimal updateRateOnSalaryClientAndInsurance(boolean isSalaryClient, boolean isInsuranceAdd, BigDecimal rate) {
        if (isSalaryClient) {
            rate = rate.subtract(BigDecimal.valueOf(2.00d)).setScale(2, RoundingMode.HALF_UP);
        }
        if (isInsuranceAdd) {
            rate = rate.subtract(BigDecimal.valueOf(3.00d)).setScale(2, RoundingMode.HALF_UP);
        }
        return rate;
    }

    private BigDecimal calculateMonthlyPayment(Integer term, BigDecimal totalAmount, BigDecimal rate) {
        var monthlyRate = getMonthlyRate(rate);
        var monthlyRateInTermPow = monthlyRate.add(BigDecimal.valueOf(1)).pow(term);
        var monthlyPayment = monthlyRate.multiply(monthlyRateInTermPow)
                .divide(monthlyRateInTermPow.subtract(BigDecimal.valueOf(1)), MathContext.DECIMAL32);
        return monthlyPayment.multiply(totalAmount).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateInsurance(BigDecimal amount, Integer term) {
        return amount.multiply(BigDecimal.valueOf(term))
                .multiply(BigDecimal.valueOf(mainRate.doubleValue() / 100))
                .setScale(2, RoundingMode.HALF_UP)
                .subtract(BigDecimal.valueOf(200000));
    }

    private BigDecimal getMonthlyRate(BigDecimal rate) {
        return BigDecimal.valueOf(rate.doubleValue() / 100 / 12);
    }
}
