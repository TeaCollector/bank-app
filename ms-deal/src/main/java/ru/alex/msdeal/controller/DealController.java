package ru.alex.msdeal.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.alex.msdeal.api.DealApi;
import ru.alex.msdeal.dto.FinishRegistrationRequestDto;
import ru.alex.msdeal.dto.LoanOfferDto;
import ru.alex.msdeal.dto.LoanStatementRequestDto;
import ru.alex.msdeal.service.DealService;


@RequiredArgsConstructor
@RestController
public class DealController implements DealApi {

    private final DealService dealService;

    @Override
    public ResponseEntity<List<LoanOfferDto>> createOffer(LoanStatementRequestDto loanStatementRequestDto) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(dealService.offer(loanStatementRequestDto));
    }

    @Override
    public ResponseEntity<Void> offerSelect(LoanOfferDto loanOfferDto) {
        dealService.selectOffer(loanOfferDto);
        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }

    @Override
    public ResponseEntity<Void> calculate(FinishRegistrationRequestDto finishRegistrationRequestDto, String statementId) {
        dealService.calculate(finishRegistrationRequestDto, statementId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }
}
