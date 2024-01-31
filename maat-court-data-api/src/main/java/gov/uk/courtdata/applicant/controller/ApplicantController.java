package gov.uk.courtdata.applicant.controller;

import gov.uk.courtdata.annotation.NotFoundApiResponse;
import gov.uk.courtdata.applicant.dto.ApplicantDTO;
import gov.uk.courtdata.applicant.dto.ApplicantHistoryDTO;
import gov.uk.courtdata.applicant.dto.RepOrderApplicantLinksDTO;
import gov.uk.courtdata.applicant.service.ApplicantHistoryService;
import gov.uk.courtdata.applicant.service.ApplicantService;
import gov.uk.courtdata.applicant.service.RepOrderApplicantLinksService;
import gov.uk.courtdata.applicant.validator.ApplicantValidationProcessor;
import gov.uk.courtdata.entity.Applicant;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Applicants", description = "Rest API for applicants")
@RequestMapping("${api-endpoints.application-domain}/applicant")
public class ApplicantController {

    private final ApplicantService applicantService;
    private final ApplicantHistoryService applicantHistoryService;
    private final RepOrderApplicantLinksService repOrderApplicantLinksService;
    private final ApplicantValidationProcessor applicantValidationProcessor;

    @GetMapping(value = "/rep-order-applicant-links/{repId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve rep order applicant links")
    @StandardApiResponseCodes
    public ResponseEntity<List<RepOrderApplicantLinksDTO>> getReOrderApplicantLinks(
            @PathVariable int repId) {
        log.info("Get Rep Order Applicant Links Request Received");
        applicantValidationProcessor.validate(repId);
        return ResponseEntity.ok(repOrderApplicantLinksService.find(repId));
    }

    @PutMapping(value = "/rep-order-applicant-links", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update rep order applicant links")
    @StandardApiResponseCodes
    public ResponseEntity<RepOrderApplicantLinksDTO> updateReOrderApplicantLinks(@RequestBody @Valid RepOrderApplicantLinksDTO repOrderApplicantLinksDTO) {
        log.info("Update Rep Order Applicant Links Request Received");
        return ResponseEntity.ok(repOrderApplicantLinksService.update(repOrderApplicantLinksDTO));
    }

    @GetMapping(value = "/applicant-history/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve applicant history")
    @StandardApiResponseCodes
    public ResponseEntity<ApplicantHistoryDTO> getApplicantHistory(
            @PathVariable int id) {
        log.info("Get Applicant History Request Received");
        return ResponseEntity.ok(applicantHistoryService.find(id));
    }

    @PutMapping(value = "/applicant-history", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update applicant history")
    @StandardApiResponseCodes
    public ResponseEntity<ApplicantHistoryDTO> updateApplicantHistory(@RequestBody @Valid ApplicantHistoryDTO applicantHistoryDTO) {
        log.info("Update Applicant History Request Received");
        return ResponseEntity.ok(applicantHistoryService.update(applicantHistoryDTO));
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve a Applicant record")
    @StandardApiResponseCodes
    @NotFoundApiResponse
    public ResponseEntity<ApplicantDTO> getApplicant(@PathVariable int id) {
        log.info("Get Applicant Request Received");
        return ResponseEntity.ok(applicantService.find(id));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update a Applicant record")
    @StandardApiResponseCodes
    @NotFoundApiResponse
    public ResponseEntity<Void> updateApplicant(@PathVariable int id, @RequestBody @Valid Applicant applicant) {
        log.info("Update Applicant Request Received");
        applicantService.update(id, applicant);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Delete a Applicant record")
    @StandardApiResponseCodes
    public ResponseEntity<Void> deleteApplicant(@PathVariable int id) {
        log.info("Delete Applicant Request Received");
        applicantService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create a Applicant record")
    @StandardApiResponseCodes
    public ResponseEntity<ApplicantDTO> createApplicant(@RequestBody @Valid ApplicantDTO applicantDTO) {
        log.info("Create Applicant Request Received");
        applicantService.create(applicantDTO);
        return ResponseEntity.ok().build();
    }
}
