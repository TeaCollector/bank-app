package ru.alex.msapigateway.service;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.alex.msapigateway.config.OpenFeignConfiguration;
import ru.alex.msapigateway.dto.LoanOfferDto;
import ru.alex.msapigateway.dto.LoanStatementRequestDto;


@FeignClient(value = "statementFeignClient", url = OpenFeignConfiguration.STATEMENT_FEIGN_URL)
public interface StatementFeignService {

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    List<LoanOfferDto> sendLoanOffer(@RequestBody LoanStatementRequestDto loanStatementRequestDto);

    @RequestMapping(method = RequestMethod.POST, value = "offer")
    void sendToStatement(@RequestBody LoanOfferDto loanOfferDto);
}
