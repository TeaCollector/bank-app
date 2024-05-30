package ru.alex.mscalc.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.alex.mscalc.entity.constant.Gender;
import ru.alex.mscalc.entity.constant.MaritalStatus;
import ru.alex.mscalc.util.RateComparator;
import ru.alex.mscalc.web.dto.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class CalculatorService {

    @Value("${app.main-rate}")
    private BigDecimal mainRate;
    @Value("${app.base-insurance}")
    private BigDecimal baseInsurance;

    private BigDecimal totalInterestPayment;

    private final EmploymentService employmentService;
    private final ClientService clientService;

    public List<LoanOfferDto> offer(LoanStatementRequestDto loanStatementRequestDto) {
        log.info("{} {} send request to receive loan offer, amount: {}, term: {}", loanStatementRequestDto.getFirstName(), loanStatementRequestDto.getLastName(),
            loanStatementRequestDto.getAmount(), loanStatementRequestDto.getTerm());
        var combination = List.of(
            true, true,
            false, false,
            true, false,
            false, true);

        var loanOfferDtoList = new ArrayList<LoanOfferDto>();

        for (int i = 0; i < combination.size() / 2; i++) {
            LoanOfferDto loanOfferDto = createLoanOffer(loanStatementRequestDto, combination.get(i), combination.get(i + 1));

            log.info("For rate: {} monthly payment is: {}, insurance enable: {}, salary client: {}, total amount: {}",
                loanOfferDto.getRate(), loanOfferDto.getMonthlyPayment(),
                loanOfferDto.getIsInsuranceEnabled(), loanOfferDto.getIsSalaryClient(),
                loanOfferDto.getTotalAmount());

            loanOfferDtoList.add(loanOfferDto);
        }

        loanOfferDtoList.sort(new RateComparator());
        log.info("Rate after sorting: {}", loanOfferDtoList.stream().map(LoanOfferDto::getRate).toList());

        return loanOfferDtoList;
    }

    private LoanOfferDto createLoanOffer(LoanStatementRequestDto loanStatementRequestDto,
                                         Boolean isSalaryClient,
                                         Boolean isInsuranceEnabled) {
        var rate = updateRate(isSalaryClient, isInsuranceEnabled, mainRate);
        var totalAmount = BigDecimal.ZERO;

        if (isInsuranceEnabled) {
            totalAmount = loanStatementRequestDto.getAmount().add(calculateInsurance(loanStatementRequestDto.getAmount()));
        } else {
            totalAmount = loanStatementRequestDto.getAmount();
        }

        return LoanOfferDto.builder()
            .statementId(UUID.randomUUID())
            .requestAmount(loanStatementRequestDto.getAmount())
            .term(loanStatementRequestDto.getTerm())
            .rate(rate)
            .isSalaryClient(isSalaryClient)
            .isInsuranceEnabled(isInsuranceEnabled)
            .totalAmount(totalAmount)
            .monthlyPayment(calculateMonthlyPayment(loanStatementRequestDto.getTerm(), totalAmount, rate, getMonthlyRate(rate)))
            .build();
    }

    public CreditDto scoreData(ScoringDataDto scoringDataDto) {
        log.info("Calculate credit condition for: {} ", scoringDataDto);
        var amount = scoringDataDto.getAmount();

        clientService.validateData(scoringDataDto);

        var rate = employmentService.calculateRateByEmployment(scoringDataDto.getEmployment(), amount, mainRate);
        var term = scoringDataDto.getTerm();

        rate = updateRate(scoringDataDto.getGender(), scoringDataDto.getBirthdate(), rate);
        rate = updateRate(scoringDataDto.getMaritalStatus(), rate);
        rate = updateRate(scoringDataDto.getIsSalaryClient(), scoringDataDto.getIsInsuranceEnabled(), rate);

        if (scoringDataDto.getIsInsuranceEnabled()) {
            amount = amount.add(calculateInsurance(amount));
        }

        var monthlyRate = getMonthlyRate(rate);

        var creditDto = CreditDto.builder()
            .isInsuranceEnabled(scoringDataDto.getIsInsuranceEnabled())
            .isSalaryClient(scoringDataDto.getIsSalaryClient())
            .term(scoringDataDto.getTerm())
            .rate(rate)
            .amount(amount)
            .monthlyPayment(calculateMonthlyPayment(scoringDataDto.getTerm(), amount, rate, monthlyRate))
            .paymentSchedule(calculatePaymentSchedule(amount, term, rate, monthlyRate))
            .psk(amount.add(totalInterestPayment).setScale(2, RoundingMode.HALF_UP))
            .build();

        log.info("Credit suggestion after all calculating: {}", creditDto);
        return creditDto;
    }

    private BigDecimal updateRate(MaritalStatus maritalStatus, BigDecimal rate) {
        log.info("Marital status: {}, rate: {}", maritalStatus, rate);
        switch (maritalStatus) {
            case SINGLE -> rate = rate.add(BigDecimal.valueOf(1.00d));
            case MARRIED -> rate = rate.subtract(BigDecimal.valueOf(3.00d));
            case WIDOWED -> rate = rate.subtract(BigDecimal.valueOf(2.00d));
        }
        log.info("Rate after marital status: {}", rate);
        return rate;
    }

    private BigDecimal updateRate(Gender gender, LocalDate birthdate, BigDecimal rate) {
        var yearOfClient = ChronoUnit.YEARS.between(birthdate, LocalDate.now());
        log.info("Client age is: {}, gender: {}, rate: {}", yearOfClient, gender, rate);
        switch (gender) {
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
        log.info("Rate after client age: {}", rate);
        return rate;
    }

    private List<PaymentScheduleElementDto> calculatePaymentSchedule(BigDecimal amount, Integer term,
                                                                     BigDecimal rate, BigDecimal monthlyRate) {
        log.info("Calculate payment schedule amount: {}, term: {}, rate: {}, monthly rate: {}", amount, term, rate, monthlyRate);
        var totalAmount = amount;
        var paymentsScheduleList = new ArrayList<PaymentScheduleElementDto>();
        var monthlyPayment = calculateMonthlyPayment(term, amount, rate, monthlyRate);
        totalInterestPayment = BigDecimal.ZERO;
        for (int i = 1; i <= term; i++) {
            var interestPayment = amount.multiply(monthlyRate);
            var debtPayment = monthlyPayment.subtract(interestPayment.setScale(2, RoundingMode.HALF_UP));
            amount = amount.subtract(debtPayment);

            var paymentScheduleElementDto = PaymentScheduleElementDto.builder()
                .number(i)
                .date(LocalDate.now().plusMonths(i))
                .totalAmount(totalAmount)
                .interestPayment(interestPayment.setScale(2, RoundingMode.HALF_UP))
                .debtPayment(debtPayment.setScale(2, RoundingMode.HALF_UP))
                .remainingDebt(amount.setScale(2, RoundingMode.HALF_UP))
                .build();

            totalInterestPayment = totalInterestPayment.add(interestPayment);
            paymentsScheduleList.add(paymentScheduleElementDto);
        }
        log.info("Interest payment: {}", totalInterestPayment);
        return paymentsScheduleList;
    }

    private BigDecimal updateRate(boolean isSalaryClient, boolean isInsuranceAdd, BigDecimal rate) {
        log.info("Client is salary: {}, is insurance enable: {}, rate: {}", isSalaryClient, isInsuranceAdd, rate);
        if (isSalaryClient) {
            rate = rate.subtract(BigDecimal.valueOf(2.00d)).setScale(2, RoundingMode.HALF_UP);
        }
        if (isInsuranceAdd) {
            rate = rate.subtract(BigDecimal.valueOf(3.00d)).setScale(2, RoundingMode.HALF_UP);
        }
        log.info("Final rate: {}", rate);
        return rate;
    }

    private BigDecimal calculateMonthlyPayment(Integer term, BigDecimal totalAmount, BigDecimal rate, BigDecimal monthlyRate) {
        log.info("Calculate monthly payment term: {}, total amount: {}, rate: {}, monthly payment: {}", term, totalAmount, rate, monthlyRate);
        var monthlyRateInTermPow = monthlyRate.add(BigDecimal.valueOf(1)).pow(term);
        var monthlyPayment = monthlyRate.multiply(monthlyRateInTermPow)
            .divide(monthlyRateInTermPow.subtract(BigDecimal.valueOf(1)), MathContext.DECIMAL32);

        return monthlyPayment.multiply(totalAmount).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateInsurance(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(mainRate.doubleValue() / 100))
            .divide(BigDecimal.valueOf(12))
            .add(baseInsurance)
            .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getMonthlyRate(BigDecimal rate) {
        log.info("Calculate monthly rate on final rate: {}", rate);
        return BigDecimal.valueOf(rate.doubleValue() / 100 / 12);
    }
}
