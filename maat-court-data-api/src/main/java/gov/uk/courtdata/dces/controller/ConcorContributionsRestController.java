package gov.uk.courtdata.dces.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.constants.CourtDataConstants;
import gov.uk.courtdata.dces.request.ConcorContributionRequest;
import gov.uk.courtdata.dces.enums.ConcorContributionStatus;
import gov.uk.courtdata.dces.service.ConcorContributionsService;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.enums.LoggingData;
import io.swagger.v3.oas.annotations.Operation;
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
import java.util.List;

@RestController
@RequestMapping("/api/internal/v1")
@Slf4j
@RequiredArgsConstructor
@XRayEnabled
@Tag(name = "Concor Contribution", description = "Rest API for Concor Contribution Files")
public class ConcorContributionsRestController {

    private final ConcorContributionsService concorContributionsService;

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Not Found.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @GetMapping(value = "/concor-contribution-files", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get a list of Contributions files")
    public ResponseEntity<List<String>> getContributionFiles(@RequestParam(name = "status") ConcorContributionStatus status,
                                                             @RequestHeader(value = CourtDataConstants.LAA_TRANSACTION_ID, required = false) String laaTransactionId) {
        MDC.put(LoggingData.LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info("Get concor files with status {} and laaTransactionId {} " ,status, laaTransactionId);
        final List<String> files = concorContributionsService.getConcorFiles(status);
        log.info("findContributionFiles count {}", files.size());
        return ResponseEntity.ok(files);
    }

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Not Found.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @PostMapping(value = "/update-contribution", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "update contribution file with concatenated concor files")
    public ResponseEntity<ErrorDTO> updateContributionFileRef(@RequestBody final ConcorContributionRequest request,
                                                              @RequestHeader(value = CourtDataConstants.LAA_TRANSACTION_ID, required = false) String laaTransactionId) {
        MDC.put(LoggingData.LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info("Update concor contribution file references with request {}", request);
        concorContributionsService.createContributionFileAndUpdateConcorContributionsStatus(request);
        return ResponseEntity.ok().build();
    }
}