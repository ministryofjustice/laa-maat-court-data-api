package gov.uk.courtdata.unlink.controller;

import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.unlink.validator.UnLinkValidationProcessor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "UnLink Case", description = "Rest APIs for Case unlinking.")
@RequestMapping("/unlink")
public class UnLinkController {

    private final UnLinkValidationProcessor unLinkValidationProcessor;

    @PostMapping("/validate")
    @Operation(description = "Validate unlinking case details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<Object> validate(
            @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId,
            @Parameter(description = "Case details data", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Unlink.class))) @RequestBody Unlink unlink) {

        MDC.put(LoggingData.LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info("LAA Status Update Request received");
        unLinkValidationProcessor.validate(unlink);

        return ResponseEntity.ok().build();
    }
}

