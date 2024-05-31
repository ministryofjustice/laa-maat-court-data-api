package gov.uk.courtdata.controller;

import gov.uk.courtdata.annotation.NotFoundApiResponse;
import gov.uk.courtdata.constants.CourtDataConstants;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.service.ResultsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "wq-result", description = "Rest API for WQ Result")
@RequestMapping("${api-endpoints.assessments-domain}/wq-result")
public class WQResultController {

    private final ResultsService resultService;

    @GetMapping(value = "/caseId/{caseId}/asnSeq/{asnSeq}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve list of WQ ResultCodes for given caseId and asnSeq")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @NotFoundApiResponse
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<List<Integer>> getResultCodeByCaseIdAndAsnSeq(
            @PathVariable int caseId,
            @PathVariable String asnSeq,
            @Parameter(description = "Used for tracing calls")
            @RequestHeader(value = CourtDataConstants.LAA_TRANSACTION_ID, required = false) String laaTransactionId) {
        log.info(String.format("Get WQ Result Codes by CaseId - %d and AsnSeq: %s {}", caseId, asnSeq));
        return ResponseEntity.ok(resultService.findWQResultCodesByCaseIdAndAsnSeq(caseId, asnSeq));
    }

}
