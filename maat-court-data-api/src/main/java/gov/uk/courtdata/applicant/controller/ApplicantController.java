package gov.uk.courtdata.applicant.controller;

import gov.uk.courtdata.annotation.NotFoundApiResponse;
import gov.uk.courtdata.annotation.StandardApiResponse;
import gov.uk.courtdata.annotation.StandardApiResponseCodes;
import gov.uk.courtdata.applicant.dto.ApplicantHistoryDTO;
import gov.uk.courtdata.applicant.dto.RepOrderApplicantLinksDTO;
import gov.uk.courtdata.applicant.dto.SendToCCLFDTO;
import gov.uk.courtdata.applicant.entity.ApplicantHistoryEntity;
import gov.uk.courtdata.applicant.service.ApplicantHistoryService;
import gov.uk.courtdata.applicant.service.ApplicantService;
import gov.uk.courtdata.applicant.service.RepOrderApplicantLinksService;
import gov.uk.courtdata.applicant.validator.ApplicantValidationProcessor;
import gov.uk.courtdata.entity.Applicant;
import gov.uk.courtdata.enums.LoggingData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
        LoggingData.MAAT_ID.putInMDC(repId);
        log.info("Get Rep Order Applicant Links Request Received");
        applicantValidationProcessor.validate(repId);
        return ResponseEntity.ok(repOrderApplicantLinksService.find(repId));
    }

    @PutMapping(value = "/rep-order-applicant-links", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update rep order applicant links")
    @StandardApiResponseCodes
    public ResponseEntity<RepOrderApplicantLinksDTO> updateReOrderApplicantLinks(@RequestBody @Valid RepOrderApplicantLinksDTO repOrderApplicantLinksDTO) {
        LoggingData.MAAT_ID.putInMDC(repOrderApplicantLinksDTO.getRepId());
        log.info("Update Rep Order Applicant Links Request Received");
        return ResponseEntity.ok(repOrderApplicantLinksService.update(repOrderApplicantLinksDTO));
    }

    @PostMapping(value = "/rep-order-applicant-links", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create rep order applicant links")
    @StandardApiResponseCodes
    public ResponseEntity<RepOrderApplicantLinksDTO> createReOrderApplicantLinks(@RequestBody @Valid RepOrderApplicantLinksDTO repOrderApplicantLinksDTO) {
        LoggingData.MAAT_ID.putInMDC(repOrderApplicantLinksDTO.getRepId());
        log.info("Create Rep Order Applicant Links Request Received");
        return ResponseEntity.ok(repOrderApplicantLinksService.create(repOrderApplicantLinksDTO));
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

    @DeleteMapping(value = "/applicant-history/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Delete a Applicant History record")
    @StandardApiResponseCodes
    public ResponseEntity<Void> deleteApplicantHistory(@PathVariable int id) {
        log.info("Delete Applicant Request Received");
        applicantHistoryService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/applicant-history", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create a Applicant History record")
    @StandardApiResponseCodes
    public ResponseEntity<ApplicantHistoryEntity> createApplicantHistory(@RequestBody @Valid ApplicantHistoryEntity applicantHistoryEntity) {
        log.info("Create Applicant Request Received");
        return ResponseEntity.ok(applicantHistoryService.create(applicantHistoryEntity));
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve a Applicant record")
    @StandardApiResponseCodes
    @NotFoundApiResponse
    public ResponseEntity<Applicant> getApplicant(@PathVariable int id) {
        log.info("Get Applicant Request Received");
        return ResponseEntity.ok(applicantService.find(id));
    }

    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update a Applicant record")
    @StandardApiResponseCodes
    @NotFoundApiResponse
    public ResponseEntity<Void> updateApplicant(@PathVariable int id, @RequestBody Map<String, Object> updatedFields) {
        log.info("Update Applicant Request Received");
        applicantService.update(id, updatedFields);
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
    @StandardApiResponse
    @NotFoundApiResponse
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Created Applicant record",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Applicant.class)))
    })
    public ResponseEntity<Applicant> createApplicant(@RequestBody @Valid Applicant applicant) {
        log.info("Create Applicant Request Received");
        return ResponseEntity.ok(applicantService.create(applicant));
    }

    @PutMapping(value = "/update-cclf", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update Send To CCLF Flag")
    @StandardApiResponseCodes
    public ResponseEntity<Void> updateSendToCCLF(@RequestBody @Valid SendToCCLFDTO sendToCCLFDTO) {
        LoggingData.MAAT_ID.putInMDC(sendToCCLFDTO.getRepId());
        log.info("Update Applicant History Request Received");
        applicantService.updateSendToCCLF(sendToCCLFDTO);
        return ResponseEntity.ok().build();
    }
}
