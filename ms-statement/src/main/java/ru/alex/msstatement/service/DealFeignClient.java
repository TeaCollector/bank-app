package ru.alex.msstatement.service;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.alex.msstatement.dto.LoanOfferDto;
import ru.alex.msstatement.dto.LoanStatementRequestDto;


@FeignClient(value = "calculatorFeignClient", url = "http://localhost:8082/deal")
public interface DealFeignClient {

    @RequestMapping(method = RequestMethod.POST, value = "statement", consumes = "application/json")
    List<LoanOfferDto> sendLoanOffer(@RequestBody LoanStatementRequestDto loanStatementRequestDto);

    @RequestMapping(method = RequestMethod.POST, value = "offer/select")
    void send(@RequestBody LoanOfferDto loanOfferDto);
}
