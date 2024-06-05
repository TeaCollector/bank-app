package ru.alex.mscalc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alex.mscalc.dto.CreditDto;
import ru.alex.mscalc.dto.LoanOfferDto;
import ru.alex.mscalc.dto.LoanStatementRequestDto;
import ru.alex.mscalc.dto.ScoringDataDto;
import ru.alex.mscalc.service.CalculatorService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("calculator")
public class CalculatorController implements CalculatorApi {

    private final CalculatorService calculatorService;

    @Override
    public ResponseEntity<List<LoanOfferDto>> offer(LoanStatementRequestDto loanStatementRequestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(calculatorService.offer(loanStatementRequestDto));
    }

    @Override
    public ResponseEntity<CreditDto> scoreData(ScoringDataDto scoringDataDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(calculatorService.scoreData(scoringDataDto));
    }
}
