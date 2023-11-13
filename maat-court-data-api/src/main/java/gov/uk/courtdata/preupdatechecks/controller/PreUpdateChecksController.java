package gov.uk.courtdata.preupdatechecks.controller;

import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.hardship.controller.StandardApiResponseCodes;
import gov.uk.courtdata.preupdatechecks.dto.ApplicantHistoryDTO;
import gov.uk.courtdata.preupdatechecks.dto.RepOrderApplicantLinksDTO;
import gov.uk.courtdata.preupdatechecks.service.ApplicantHistoryService;
import gov.uk.courtdata.preupdatechecks.service.RepOrderApplicantLinksService;
import gov.uk.courtdata.preupdatechecks.validator.PreUpdateChecksValidationProcessor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Application Pre Update Checks", description = "Rest API for application pre update checks")
@RequestMapping("${api-endpoints.assessments-domain}/application/pre-update-checks")
public class PreUpdateChecksController {

    private final ApplicantHistoryService applicantHistoryService;
    private final RepOrderApplicantLinksService repOrderApplicantLinksService;
    private final PreUpdateChecksValidationProcessor preUpdateChecksValidationProcessor;

    @GetMapping(value = "/rep-order-applicant-links/repId/{repId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve rep order applicant links")
    @StandardApiResponseCodes
    @ApiResponse(responseCode = "404",
            description = "Not Found.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    public ResponseEntity<List<RepOrderApplicantLinksDTO>> getReOrderApplicantLinks(
            @PathVariable int repId) {
        log.info("Get Rep Order Applicant Links Request Received");
        preUpdateChecksValidationProcessor.validate(repId);
        return ResponseEntity.ok(repOrderApplicantLinksService.getRepOrderApplicantLinks(repId));
    }

    @PutMapping(value = "/applicant-history", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update applicant history")
    @StandardApiResponseCodes
    public ResponseEntity<ApplicantHistoryDTO> updateApplicantHistory(@RequestBody @Valid ApplicantHistoryDTO applicantHistoryDTO) {
        log.info("Update Applicant History Request Received");
        return ResponseEntity.ok(applicantHistoryService.update(applicantHistoryDTO));
    }
}
