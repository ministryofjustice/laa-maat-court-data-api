package gov.uk.courtdata.dces.controller;

import gov.uk.courtdata.constants.CourtDataConstants;
import gov.uk.courtdata.dces.enums.ConcorContributionStatus;
import gov.uk.courtdata.dces.request.ConcorContributionRequest;
import gov.uk.courtdata.dces.service.ConcorContributionsService;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.enums.LoggingData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import java.util.List;

@RestController
@RequestMapping("/api/internal/v1")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Contributions", description = "Rest API for Concor Contribution Files")
public class ConcorContributionsRestController {

    private final ConcorContributionsService concorContributionsService;

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Not Found.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @GetMapping(value = "/concor-contribution-files", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get a list of Contributions files")
    public ResponseEntity<List<String>> getConcorContributionFiles(@RequestParam(name = "status") final ConcorContributionStatus status,
                                                                   @RequestHeader(value = CourtDataConstants.LAA_TRANSACTION_ID, required = false)
                                                                   final String laaTransactionId) {
        MDC.put(LoggingData.LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info("Get Concor contribution files with status {} and laaTransactionId {} " ,status, laaTransactionId);
        final List<String> concorFiles = concorContributionsService.getConcorFiles(status);
        log.info("findContributionFiles count {}", concorFiles.size());
        return ResponseEntity.ok(concorFiles);
    }

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Not Found.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @PostMapping(value = "/update-contribution", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "update contribution file with concatenated concor files")
    public ResponseEntity<Boolean> updateContributionFileStatus(@RequestBody @NotEmpty final ConcorContributionRequest request,
                                                                @RequestHeader(value = CourtDataConstants.LAA_TRANSACTION_ID, required = false)
                                                                final String laaTransactionId) {
        MDC.put(LoggingData.LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info("Update concor contribution file references with request {}", request);
        boolean response = concorContributionsService.createContributionAndUpdateConcorStatus(request);
        return ResponseEntity.ok(response);
    }
}