package ru.alex.msapigateway.api;

import java.util.List;
import javax.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alex.msapigateway.dto.*;


@Tag(name = "Api gateway controller", description = "For resending all request")
@RequestMapping
public interface GatewayApi {


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
    @PostMapping("statement")
    ResponseEntity<List<LoanOfferDto>> statement(@Valid @RequestBody LoanStatementRequestDto loanStatementRequestDto);

    @Operation(summary = "Select loan offer")
    @ApiResponses(value =
    @ApiResponse(
        responseCode = "200",
        description = "Success")
    )
    @PostMapping("statement/offer")
    ResponseEntity<Void> selectOffer(@Valid @RequestBody LoanOfferDto scoringDataDto);

    @Operation(summary = "Finish registration and calculate full credit purpose")
    @ApiResponses(value =
    @ApiResponse(
        responseCode = "200",
        description = "Void",
        content = { @Content(
            mediaType = "application/json")
        })
    )
    @PostMapping("deal/calculate/{statementId}")
    ResponseEntity<Void> calculate(@Valid @RequestBody FinishRegistrationRequestDto finishRegistrationRequestDto,
                                   @Parameter(name = "statementId") @PathVariable String statementId);

    @Operation(summary = "Sending documents")
    @ApiResponses(value =
    @ApiResponse(
        responseCode = "200",
        description = "Void")
    )
    @PostMapping("deal/document/{statementId}/send")
    ResponseEntity<Void> sendDocument(@Parameter(name = "statementId") @PathVariable String statementId);

    @Operation(summary = "Sign documents")
    @ApiResponses(value =
    @ApiResponse(
        responseCode = "200",
        description = "Void",
        content = { @Content(
            mediaType = "application/json")
        })
    )
    @PostMapping("deal/document/{statementId}/sign")
    ResponseEntity<Void> signDocument(@Parameter(name = "statementId") @PathVariable String statementId);

    @Operation(summary = "Code for sign documents")
    @ApiResponses(value =
    @ApiResponse(
        responseCode = "200",
        description = "Void",
        content = { @Content(
            mediaType = "application/json")
        })
    )
    @PostMapping("deal/document/{statementId}/code")
    ResponseEntity<Void> codeSign(@Valid @RequestBody SesCodeDto sesCode,
                                  @Parameter(name = "statementId") @PathVariable String statementId);

    @Operation(summary = "[Admin]Get statement")
    @ApiResponses(value =
    @ApiResponse(
        responseCode = "200",
        description = "Void",
        content = { @Content(
            mediaType = "application/json")
        })
    )
    @GetMapping("deal/admin/statement/{statementId}")
    ResponseEntity<StatementDto> getStatement(@Parameter(name = "statementId") @PathVariable String statementId);

    @Operation(summary = "[Admin]Get all statements")
    @ApiResponses(value =
    @ApiResponse(
        responseCode = "200",
        description = "Void",
        content = { @Content(
            mediaType = "application/json")
        })
    )
    @GetMapping("deal/admin/statements")
    ResponseEntity<List<StatementDto>> getAllStatements();
}
