package ru.alex.mscalc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.alex.mscalc.util.RateComparator;
import ru.alex.mscalc.web.dto.CreditDto;
import ru.alex.mscalc.web.dto.LoanOfferDto;
import ru.alex.mscalc.web.dto.LoanStatementRequestDto;
import ru.alex.mscalc.web.dto.ScoringDataDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CalculatorService {

    @Value("${app.main-rate}")
    private Integer mainRate;

    private final SalaryClientService salaryClientService;

    public List<LoanOfferDto> offer(LoanStatementRequestDto loanStatementRequestDto) {
        var isInsuranceAdd = true;
        var isSalaryClient = salaryClientService.checking(new Client(loanStatementRequestDto.getEmail(),
                loanStatementRequestDto.getFirstName(), loanStatementRequestDto.getLastName(),
                loanStatementRequestDto.getMiddleName(), loanStatementRequestDto.getBirthDate(),
                loanStatementRequestDto.getPassportSeries(), loanStatementRequestDto.getPassportNumber()));
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
                    rate = new BigDecimal(mainRate - 5).setScale(2, RoundingMode.HALF_UP);
                    loanOfferDto.setRate(rate);
                    totalAmount = loanStatementRequestDto.getAmount().add(insurance);
                    isInsuranceAdd = false;
                } else {
                    loanOfferDto.setIsInsuranceEnabled(false);
                    rate = new BigDecimal(mainRate - 2).setScale(2, RoundingMode.HALF_UP);
                    loanOfferDto.setRate(rate);
                    totalAmount = loanStatementRequestDto.getAmount();
                    isSalaryClient = false;
                    isInsuranceAdd = true;
                }
            } else {
                loanOfferDto.setIsSalaryClient(false);
                if (isInsuranceAdd) {
                    loanOfferDto.setIsInsuranceEnabled(true);
                    rate = new BigDecimal(mainRate - 3).setScale(2, RoundingMode.HALF_UP);
                    loanOfferDto.setRate(rate);
                    totalAmount = loanStatementRequestDto.getAmount().add(insurance);
                    isInsuranceAdd = false;
                } else {
                    loanOfferDto.setIsInsuranceEnabled(false);
                    rate = new BigDecimal(mainRate).setScale(2, RoundingMode.HALF_UP);
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

    public CreditDto validate(ScoringDataDto scoringDataDto) {
        return null;
    }

    private BigDecimal calculateMonthlyPayment(Integer term, BigDecimal totalAmount, BigDecimal rate) {
        double monthlyRate = rate.doubleValue() / 100 / 12;
        double monthlyRateInTermPow = Math.pow(1 + monthlyRate, term);
        double monthlyPayment = monthlyRate * monthlyRateInTermPow / (monthlyRateInTermPow - 1);
        return new BigDecimal(monthlyPayment).multiply(totalAmount).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateInsurance(BigDecimal amount, Integer term) {
        return amount.multiply(BigDecimal.valueOf(term))
                .multiply(BigDecimal.valueOf((double) mainRate / 100))
                .setScale(2, RoundingMode.HALF_UP)
                .subtract(BigDecimal.valueOf(100000));
    }
}
