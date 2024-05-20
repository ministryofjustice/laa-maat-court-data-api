package gov.uk.courtdata.link.controller;

import gov.uk.courtdata.dto.ErrorDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@Slf4j
@RequiredArgsConstructor
@Tag(name = "Link Case", description = "Rest APIs for Case linking.")
@RequestMapping("/link")
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

    LoggingData.MAAT_ID.putInMDC(caseDetailsValidate.getMaatId());
    LoggingData.CASE_URN.putInMDC(caseDetailsValidate.getCaseUrn());

    log.info("Validate link request.");
    preConditionsValidator.validate(caseDetailsValidate);

    return ResponseEntity.ok().build();
  }
}