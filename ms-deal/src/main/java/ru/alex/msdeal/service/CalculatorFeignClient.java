package ru.alex.msdeal.service;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.alex.msdeal.dto.LoanOfferDto;
import ru.alex.msdeal.dto.LoanStatementRequestDto;
import ru.alex.msdeal.dto.ScoringDataDto;


@FeignClient(value = "calculatorFeignClient", url = "http://localhost:8081/calculator")
public interface CalculatorFeignClient {

    @RequestMapping(method = RequestMethod.POST, value = "offers", consumes = "application/json")
    List<LoanOfferDto> calculateLoanOffer(@RequestBody LoanStatementRequestDto loanStatementRequestDto);

    @RequestMapping(method = RequestMethod.POST, value = "calc")
    void calculateCreditOffer(@RequestBody ScoringDataDto scoringDataDto);
}
