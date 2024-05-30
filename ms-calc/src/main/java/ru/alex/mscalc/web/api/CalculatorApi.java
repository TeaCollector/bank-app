package ru.alex.mscalc.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.alex.mscalc.web.dto.CreditDto;
import ru.alex.mscalc.web.dto.LoanOfferDto;
import ru.alex.mscalc.web.dto.LoanStatementRequestDto;
import ru.alex.mscalc.web.dto.ScoringDataDto;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Calculator controller", description = "For calculate all purpose")
@RequestMapping("calculator/")
public interface CalculatorApi {

    @Operation(summary = "Receive offer for loan")
    @ApiResponses(value =
    @ApiResponse(
            responseCode = "200",
            description = "List with offers",
            content = {@Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(
                            implementation = LoanOfferDto.class))
            )
            })
    )
    @PostMapping("offers")
    ResponseEntity<List<LoanOfferDto>> offer(@Valid @RequestBody LoanStatementRequestDto loanStatementRequestDto);


    @Operation(summary = "Receive credit offer")
    @ApiResponses(value =
    @ApiResponse(
            responseCode = "200",
            description = "Credit suggestion with monthly payment",
            content = {@Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(
                            implementation = CreditDto.class))
            )
            })
    )
    @PostMapping("calc")
    ResponseEntity<CreditDto> scoreData(@Valid @RequestBody ScoringDataDto scoringDataDto);
}
