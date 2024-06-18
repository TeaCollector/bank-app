package ru.alex.msstatement.api;

import java.util.List;
import javax.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.alex.msstatement.dto.LoanOfferDto;
import ru.alex.msstatement.dto.LoanStatementRequestDto;


@Tag(name = "Statement controller", description = "For pre scoring all purpose")
@RequestMapping("statement/")
public interface StatementApi {

    @Operation(summary = "Calculate loan offer by loan request")
    @ApiResponses(value =
    @ApiResponse(
        responseCode = "200",
        description = "List with offers",
        content = { @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(
                implementation = LoanOfferDto.class))
        )
        })
    )
    @PostMapping
    ResponseEntity<List<LoanOfferDto>> statement(@Valid @RequestBody LoanStatementRequestDto loanStatementRequestDto);


    @Operation(summary = "Select loan offer")
    @ApiResponses(value =
    @ApiResponse(
        responseCode = "200",
        description = "Success")
    )
    @PostMapping("offer")
    ResponseEntity<Void> selectOffer(@Valid @RequestBody LoanOfferDto scoringDataDto);
}
