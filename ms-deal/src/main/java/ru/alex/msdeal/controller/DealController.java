package ru.alex.msdeal.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.alex.msdeal.api.DealApi;
import ru.alex.msdeal.dto.*;
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

    @Override
    public ResponseEntity<Void> sendDocument(String statementId) {
        dealService.sendDocument(statementId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }

    @Override
    public ResponseEntity<Void> signDocument(String statementId) {
        dealService.signDocument(statementId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }

    @Override
    public ResponseEntity<Void> codeSign(SesCodeDto sesCodeDto, String statementId) {
        dealService.signCode(sesCodeDto, statementId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }

    @Override
    public ResponseEntity<StatementDto> getStatement(String statementId) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(dealService.getStatementById(statementId));
    }

    @Override
    public ResponseEntity<List<StatementDto>> getAllStatements() {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(dealService.getAllStatements());
    }
}
