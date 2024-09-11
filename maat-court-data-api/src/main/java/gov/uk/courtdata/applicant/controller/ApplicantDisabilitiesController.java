package gov.uk.courtdata.applicant.controller;

import gov.uk.courtdata.annotation.StandardApiResponseCodes;
import gov.uk.courtdata.applicant.dto.ApplicantDisabilitiesDTO;
import gov.uk.courtdata.applicant.service.ApplicantDisabilitiesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Applicant Disabilities", description = "Rest API for applicant disabilities")
@RequestMapping("${api-endpoints.applicant-domain}/applicant-disabilities")
public class ApplicantDisabilitiesController {

    private final ApplicantDisabilitiesService applicantDisabilitiesService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve applicant disabilities")
    @StandardApiResponseCodes
    public ResponseEntity<ApplicantDisabilitiesDTO> getApplicantDisabilities(
            @PathVariable int id) {
        log.info("Get Applicant Disabilities Request Received for id {}", id);
        return ResponseEntity.ok(applicantDisabilitiesService.find(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create applicant disabilities")
    @StandardApiResponseCodes
    public ResponseEntity<ApplicantDisabilitiesDTO> createApplicantDisabilities(@RequestBody @Valid ApplicantDisabilitiesDTO applicantDisabilitiesDTO) {
        log.info("Create Applicant Disabilities Request Received");
        return ResponseEntity.ok(applicantDisabilitiesService.create(applicantDisabilitiesDTO));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update applicant disabilities")
    @StandardApiResponseCodes
    public ResponseEntity<ApplicantDisabilitiesDTO> updateApplicantDisabilities(@RequestBody @Valid ApplicantDisabilitiesDTO applicantDisabilitiesDTO) {
        log.info("Update Applicant Disabilities Request Received for id {}"+applicantDisabilitiesDTO.getId());
        return ResponseEntity.ok(applicantDisabilitiesService.update(applicantDisabilitiesDTO));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(description = "Delete applicant disability")
    @StandardApiResponseCodes
    public ResponseEntity<Void> deleteApplicantDisability(@PathVariable int id) {
        log.info("Get Applicant Disabilities Request Received for id {}", id);
        applicantDisabilitiesService.delete(id);
        return ResponseEntity.ok().build();
    }
}
