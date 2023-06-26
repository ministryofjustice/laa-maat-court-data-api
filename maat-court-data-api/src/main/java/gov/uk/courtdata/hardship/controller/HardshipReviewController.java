package gov.uk.courtdata.hardship.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.hardship.service.HardshipReviewService;
import gov.uk.courtdata.hardship.validator.HardshipReviewValidationProcessor;
import gov.uk.courtdata.model.hardship.CreateHardshipReview;
import gov.uk.courtdata.model.hardship.UpdateHardshipReview;
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
@RequestMapping("${api-endpoints.assessments-domain}/hardship")
@Slf4j
@RequiredArgsConstructor
@XRayEnabled
@Tag(name = "Hardship Reviews", description = "Rest API for hardship reviews")
public class HardshipReviewController {

    private final HardshipReviewService hardshipReviewService;
    private final HardshipReviewValidationProcessor validationProcessor;

    @GetMapping(value = "/{hardshipId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve a hardship review record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = HardshipReviewDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<HardshipReviewDTO> getHardship(
            @PathVariable int hardshipId,
            @Parameter(description = "Used for tracing calls")
            @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        MDC.put(LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info("Get Hardship Review Request Received");

        validationProcessor.validate(hardshipId);
        return ResponseEntity.ok(hardshipReviewService.findHardshipReview(hardshipId));
    }

    @GetMapping(value = "repId/{repId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve a hardship review record by repId")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = HardshipReviewDTO.class)))
    @ApiResponse(responseCode = "404", description = "Not Found.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<HardshipReviewDTO> getHardshipByRepId(@PathVariable int repId,
                                                                @Parameter(description = "Used for tracing calls")
                                                                @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {
        MDC.put(LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info("Get Hardship Review by repId = {}", repId);
        return ResponseEntity.ok(hardshipReviewService.findHardshipReviewByRepId(repId));
    }

    @GetMapping(value = "repId/{repId}/detailType/{detailType}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve a hardship review record by repId and detail type")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = HardshipReviewDTO.class)))
    @ApiResponse(responseCode = "404", description = "Not Found.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<HardshipReviewDTO> getHardshipByDetailType(@PathVariable int repId,
                                                                     @PathVariable String detailType,
                                                                     @Parameter(description = "Used for tracing calls")
                                                                     @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {
        MDC.put(LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info("Get Hardship Review by detail type = {} and repId = {}", detailType, repId);
        return ResponseEntity.ok(hardshipReviewService.findHardshipReviewByDetailType(detailType, repId));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve a hardship review record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = HardshipReviewDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<HardshipReviewDTO> createHardship(
            @Parameter(description = "Hardship review data", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CreateHardshipReview.class))) @RequestBody CreateHardshipReview hardshipReview,
            @Parameter(description = "Used for tracing calls")
            @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        MDC.put(LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info("Create Hardship Review Request Received");

        validationProcessor.validate(hardshipReview);
        return ResponseEntity.ok(hardshipReviewService.createHardshipReview(hardshipReview));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update a hardship review record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = HardshipReviewDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<HardshipReviewDTO> updateHardship(
            @Parameter(description = "Hardship review data", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UpdateHardshipReview.class))) @RequestBody UpdateHardshipReview hardshipReview,
            @Parameter(description = "Used for tracing calls")
            @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        MDC.put(LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info("Update Hardship Review Request Received");

        validationProcessor.validate(hardshipReview);
        return ResponseEntity.ok(hardshipReviewService.updateHardshipReview(hardshipReview));
    }
}
