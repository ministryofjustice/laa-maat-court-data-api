package gov.uk.courtdata.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.service.ResultsService;
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

import java.util.List;

import static gov.uk.courtdata.enums.LoggingData.LAA_TRANSACTION_ID;

@RestController
@RequestMapping("${api-endpoints.assessments-domain}/wq-result")
@Slf4j
@RequiredArgsConstructor
@XRayEnabled
@Tag(name = "wq-result", description = "Rest API for WQ Result")
public class WQResultController {

    private final ResultsService resultService;

    @GetMapping(value = "/caseId/{caseId}/asnSeq/{asnSeq}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve list of WQ ResultCodes for given caseId and asnSeq")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "404", description = "Not Found.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<List<Integer>> getResultCodeByCaseIdAndAsnSeq(
            @PathVariable int caseId,
            @PathVariable String asnSeq,
            @Parameter(description = "Used for tracing calls")
            @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {
        MDC.put(LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info(String.format("Get WQ Result Codes by CaseId - %d and AsnSeq: %s {}", caseId, asnSeq));
        return ResponseEntity.ok(resultService.findWQResultCodesByCaseIdAndAsnSeq(caseId, asnSeq));
    }

}
