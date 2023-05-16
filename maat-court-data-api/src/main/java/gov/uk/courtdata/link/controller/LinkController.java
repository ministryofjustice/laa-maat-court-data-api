package gov.uk.courtdata.link.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.common.dto.ErrorDTO;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.link.validator.PreConditionsValidator;
import gov.uk.courtdata.model.CaseDetailsValidate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/link")
@Slf4j
@XRayEnabled
@RequiredArgsConstructor
@Tag(name = "Link Case", description = "Rest APIs for Case linking.")
public class LinkController {

    private final PreConditionsValidator preConditionsValidator;

    @PostMapping("/validate")
    @Operation(description = "Validate linking case details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })

    public ResponseEntity<Object> validate(
            @Parameter(description = "Case details data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CaseDetailsValidate.class)))
            @RequestBody CaseDetailsValidate caseDetailsValidate,
            @Parameter(description = "Used for tracing calls") @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        MDC.put(LoggingData.LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        MDC.put(LoggingData.MAATID.getValue(), caseDetailsValidate.getMaatId() != null ? caseDetailsValidate.getMaatId().toString() : "");

        log.info("Validate link request.");
        preConditionsValidator.validate(caseDetailsValidate);

        return ResponseEntity.ok().build();
    }
}