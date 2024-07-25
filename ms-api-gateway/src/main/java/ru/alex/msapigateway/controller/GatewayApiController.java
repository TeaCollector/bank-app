package ru.alex.msapigateway.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.alex.msapigateway.api.GatewayApi;
import ru.alex.msapigateway.dto.*;
import ru.alex.msapigateway.service.DealFeignService;
import ru.alex.msapigateway.service.StatementFeignService;


@RequiredArgsConstructor
@RestController
public class GatewayApiController implements GatewayApi {
    private final DealFeignService dealFeignService;
    private final StatementFeignService statementFeignService;

    @Override
    public ResponseEntity<List<LoanOfferDto>> statement(LoanStatementRequestDto loanStatementRequestDto) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(statementFeignService.sendLoanOffer(loanStatementRequestDto));
    }

    @Override
    public ResponseEntity<Void> selectOffer(LoanOfferDto scoringDataDto) {
        statementFeignService.sendToStatement(scoringDataDto);
        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }

    @Override
    public ResponseEntity<Void> calculate(FinishRegistrationRequestDto finishRegistrationRequestDto, String statementId) {
        dealFeignService.calculate(finishRegistrationRequestDto, statementId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }

    @Override
    public ResponseEntity<Void> sendDocument(String statementId) {
        dealFeignService.sendDocument(statementId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }

    @Override
    public ResponseEntity<Void> signDocument(String statementId) {
        dealFeignService.signDocument(statementId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }

    @Override
    public ResponseEntity<Void> codeSign(SesCodeDto sesCode, String statementId) {
        dealFeignService.codeSign(statementId, sesCode);
        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }

    @Override
    public ResponseEntity<StatementDto> getStatement(String statementId) {
        return dealFeignService.getStatement(statementId);
    }

    @Override
    public ResponseEntity<List<StatementDto>> getAllStatements() {
        return dealFeignService.getStatements();
    }
}
