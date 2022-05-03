package gov.uk.courtdata.assessment.controller;

import gov.uk.courtdata.assessment.service.PostProcessingService;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.enums.LoggingData;
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

@RestController
@RequestMapping("${api-endpoints.assessments-domain}/post-processing")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Post Processing", description = "Rest API for assessment post-processing")
public class PostProcessingController {

    private final PostProcessingService postProcessingService;

    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(
            responseCode = "400",
            description = "Bad Request.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(
            responseCode = "500",
            description = "Server Error.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    @PostMapping(value = "/{repID}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Perform post-processing for a given RepID")
    public ResponseEntity<Object> doPostProcessing(
            @PathVariable Integer repID,
            @Parameter(description = "Used for tracing calls")
            @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        MDC.put(LoggingData.LAA_TRANSACTION_ID.getValue(), laaTransactionId);

        log.info("Assessment Post-Processing Request Received for RepID: {}", repID);
        postProcessingService.execute(repID);
        log.info("Assessment Post-Processing Request Complete");
        return ResponseEntity.ok().build();
    }
}
