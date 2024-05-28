package gov.uk.courtdata.controller;

import gov.uk.courtdata.annotation.NotFoundApiResponse;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.model.UpdateCCOutcome;
import gov.uk.courtdata.model.UpdateSentenceOrder;
import gov.uk.courtdata.service.CrownCourtOutcomeService;
import gov.uk.courtdata.validator.MaatIdValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "crown-court", description = "Rest API for invoking stored procedures related to Crown Court")
@RequestMapping("${api-endpoints.assessments-domain}/crown-court")
public class CrownCourtController {

    private final CrownCourtOutcomeService crownCourtOutcomeService;
    private final MaatIdValidator maatIdValidator;

    @PutMapping(value = "/updateCCOutcome", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update Crown Court Outcome")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @NotFoundApiResponse
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Object> updateCCOutcome(@RequestBody UpdateCCOutcome updateCCOutcome) {
        LoggingData.MAAT_ID.putInMDC(updateCCOutcome.getRepId());
        log.debug("Update CC Outcome for repId : {}", updateCCOutcome.getRepId());
        maatIdValidator.validate(updateCCOutcome.getRepId());
        crownCourtOutcomeService.updateCCOutcome(updateCCOutcome);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/update-cc-sentence", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update Crown Court Sentence Order Date")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @NotFoundApiResponse
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Object> updateCCSentenceOrderDate(@RequestBody UpdateSentenceOrder updateSentenceOrder) {
        LoggingData.MAAT_ID.putInMDC(updateSentenceOrder.getRepId());
        log.debug("Update Crown Court Sentence Order Date for repId : {}", updateSentenceOrder.getRepId());
        maatIdValidator.validate(updateSentenceOrder.getRepId());
        crownCourtOutcomeService.updateCCSentenceOrderDate(updateSentenceOrder);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/update-appeal-cc-sentence", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update Appeal Crown Court Sentence Order Date")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @NotFoundApiResponse
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Object> updateAppealCCSentenceOrderDate(@RequestBody UpdateSentenceOrder updateSentenceOrder) {
        LoggingData.MAAT_ID.putInMDC(updateSentenceOrder.getRepId());
        log.debug("Update Appeal Crown Court Sentence Order Date for repId : {}", updateSentenceOrder.getRepId());
        maatIdValidator.validate(updateSentenceOrder.getRepId());
        crownCourtOutcomeService.updateAppealCCSentenceOrderDate(updateSentenceOrder);
        return ResponseEntity.ok().build();
    }
}
