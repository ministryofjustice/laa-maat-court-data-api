package gov.uk.courtdata.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.model.UpdateCCOutcome;
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

@RestController
@RequestMapping("${api-endpoints.assessments-domain}/crown-court")
@Slf4j
@RequiredArgsConstructor
@XRayEnabled
@Tag(name = "crown-court", description = "Rest API for Crown Court")
public class CrownCourtController {

    private final CrownCourtOutcomeService crownCourtOutcomeService;
    private final MaatIdValidator maatIdValidator;

    @PutMapping(value = "/updateCCOutcome", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update Crown Court Outcome")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "404", description = "Not Found.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Object> updateCCOutcome(@RequestBody UpdateCCOutcome updateCCOutcome) {
        log.debug("Update CC Outcome for repId : {}", updateCCOutcome.getRepId());
        maatIdValidator.validate(updateCCOutcome.getRepId());
        crownCourtOutcomeService.update(updateCCOutcome);
        return ResponseEntity.ok().build();
    }

}
