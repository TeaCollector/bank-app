package ru.alex.msstatement.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.alex.msstatement.api.StatementApi;
import ru.alex.msstatement.dto.LoanOfferDto;
import ru.alex.msstatement.dto.LoanStatementRequestDto;
import ru.alex.msstatement.service.DealFeignClient;


@RestController
@RequiredArgsConstructor
public class StatementController implements StatementApi {

    private final DealFeignClient dealFeignClient;

    @Override
    public ResponseEntity<List<LoanOfferDto>> statement(LoanStatementRequestDto loanStatementRequestDto) {
        var loanOffersDtos = dealFeignClient.sendLoanOffer(loanStatementRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
            .body(loanOffersDtos);
    }

    @Override
    public ResponseEntity<Void> selectOffer(LoanOfferDto loanOfferDto) {
        dealFeignClient.selectOffer(loanOfferDto);
        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }
}
