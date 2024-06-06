package ru.alex.msdeal.api;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.alex.msdeal.dto.FinishRegistrationRequestDto;
import ru.alex.msdeal.dto.LoanOfferDto;
import ru.alex.msdeal.dto.LoanStatementRequestDto;


@Tag(name = "Deal controller", description = "For save and send all purpose")
@RequestMapping("deal")
public interface DealApi {

    @Operation(summary = "Receive and save offer for loan")
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
    @PostMapping("statement")
    ResponseEntity<List<LoanOfferDto>> createOffer(@RequestBody LoanStatementRequestDto loanStatementRequestDto);


    @Operation(summary = "Choose purpose")
    @ApiResponses(value =
    @ApiResponse(
        responseCode = "200",
        description = "Void",
        content = { @Content(
            mediaType = "application/json")
        })
    )
    @PostMapping("offer/select")
    ResponseEntity<Void> offerSelect(@RequestBody LoanOfferDto loanOfferDto);

    @Operation(summary = "Finish registration and calculate full credit purpose")
    @ApiResponses(value =
    @ApiResponse(
        responseCode = "200",
        description = "Void",
        content = { @Content(
            mediaType = "application/json")
        })
    )
    @PostMapping("calculate/{statementId}")
    ResponseEntity<Void> calculate(@RequestBody FinishRegistrationRequestDto finishRegistrationRequestDto,
                                   @Parameter(name = "statementId") @PathVariable String statementId);
}
