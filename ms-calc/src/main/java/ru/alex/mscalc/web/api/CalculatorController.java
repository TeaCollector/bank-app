package ru.alex.mscalc.web.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alex.mscalc.service.CalculatorService;
import ru.alex.mscalc.web.dto.CreditDto;
import ru.alex.mscalc.web.dto.LoanOfferDto;
import ru.alex.mscalc.web.dto.LoanStatementRequestDto;
import ru.alex.mscalc.web.dto.ScoringDataDto;

import javax.validation.Valid;
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
