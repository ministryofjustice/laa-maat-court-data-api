package gov.uk.courtdata.hardship.controller;

import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.hardship.service.HardshipReviewService;
import gov.uk.courtdata.validator.HardshipReviewIdValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static gov.uk.courtdata.enums.LoggingData.LAA_TRANSACTION_ID;

@RestController
@RequestMapping("/hardship")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Hardship Reviews", description = "Rest API for hardship reviews")
public class HardshipReviewController {

    private final HardshipReviewService hardshipReviewService;
    private final HardshipReviewIdValidator hardshipReviewIdValidator;

    @GetMapping(value = "/{hardshipId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve a hardship review record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = HardshipReviewDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Object> getHardship(
            @PathVariable int hardshipId,
            @Parameter(description = "Used for tracing calls")
            @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        MDC.put(LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info("Get Hardship Review Request Received");

        hardshipReviewIdValidator.validate(hardshipId);
        return ResponseEntity.ok(hardshipReviewService.find(hardshipId));
    }
}
