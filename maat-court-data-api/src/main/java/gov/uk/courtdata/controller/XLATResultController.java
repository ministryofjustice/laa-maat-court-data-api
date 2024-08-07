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
@Tag(name = "xlat-result", description = "Rest API for XLAT Result")
@RequestMapping("${api-endpoints.assessments-domain}/xlat-result")
public class XLATResultController {

    private final ResultsService resultService;

    @GetMapping(value = "/cc-imprisonment", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve list of XLAT ResultCodes for CC Imprisonment")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @NotFoundApiResponse
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<List<Integer>> getResultCodesForCCImprisonment(
            @Parameter(description = "Used for tracing calls")
            @RequestHeader(value = CourtDataConstants.LAA_TRANSACTION_ID, required = false) String laaTransactionId) {
        log.info("Get XLAT Result Codes for CC Imprisonment");
        return ResponseEntity.ok(resultService.findXLATResultCodesForCCImprisonment());
    }

    @GetMapping(value = "/cc-bench-warrant", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve list of XLAT ResultCodes for CC Bench Warrant")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @NotFoundApiResponse
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<List<Integer>> getResultCodesForCCBenchWarrant(
            @Parameter(description = "Used for tracing calls")
            @RequestHeader(value = CourtDataConstants.LAA_TRANSACTION_ID, required = false) String laaTransactionId) {
        log.info("Get XLAT Result Codes for CC Bench Warrant");
        return ResponseEntity.ok(resultService.findXLATResultCodesForCCBenchWarrant());
    }

    @GetMapping(value = "/wqType/{wqType}/subType/{subType}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve list of WQ ResultCodes for given caseId and asnSeq")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @NotFoundApiResponse
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<List<Integer>> getResultCodesByWqTypeAndSubType(
            @PathVariable int wqType,
            @PathVariable int subType,
            @Parameter(description = "Used for tracing calls")
            @RequestHeader(value = CourtDataConstants.LAA_TRANSACTION_ID, required = false) String laaTransactionId) {
        log.info(String.format("Get XLAT Result Codes by WqType - %d and SubType: %s {}", wqType, subType));
        return ResponseEntity.ok(resultService.findXLATResultCodesByWQTypeAndSubTypeCode(wqType, subType));
    }

}
