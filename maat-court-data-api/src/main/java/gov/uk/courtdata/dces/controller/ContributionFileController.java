package gov.uk.courtdata.dces.controller;

import gov.uk.courtdata.annotation.NotFoundApiResponse;
import gov.uk.courtdata.annotation.StandardApiResponse;
import gov.uk.courtdata.dces.response.ContributionFileErrorResponse;
import gov.uk.courtdata.dces.response.ContributionFileResponse;
import gov.uk.courtdata.dces.service.ContributionFileService;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Debt Collection Enforcement", description = "Rest API for Debt Collection Enforcement Service")
@RequestMapping("${api-endpoints.debt-collection-enforcement-domain}/contribution-file")
@Slf4j
@RequiredArgsConstructor
public class ContributionFileController {
    private final ContributionFileService contributionFileService;

    @Operation(description = "Retrieve a single contribution file by its unique ID")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ContributionFileResponse.class)))
    @StandardApiResponse
    @NotFoundApiResponse
    @GetMapping("/{contributionFileId}")
    public ResponseEntity<ContributionFileResponse> getContributionFile(@PathVariable int contributionFileId) {
        return ResponseEntity.ok(contributionFileService.getContributionFile(contributionFileId).orElseThrow(
                () -> new RequestedObjectNotFoundException("Contribution file not found"))); // to get ErrorDTO
    }

    @Operation(description = "Retrieve the collection of contribution file errors by contribution file ID")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = ContributionFileErrorResponse.class))))
    @StandardApiResponse
    @GetMapping("/{contributionFileId}/error")
    public ResponseEntity<List<ContributionFileErrorResponse>> getAllContributionFileError(@PathVariable int contributionFileId) {
        return ResponseEntity.ok(contributionFileService.getAllContributionFileError(contributionFileId));
    }

    @Operation(description = "Retrieve a single contribution file error by its unique IDs")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ContributionFileErrorResponse.class)))
    @StandardApiResponse
    @NotFoundApiResponse
    @GetMapping("/{contributionFileId}/error/{contributionId}")
    public ResponseEntity<ContributionFileErrorResponse> getContributionFileError(@PathVariable int contributionFileId,
                                                                                  @PathVariable int contributionId) {
        return ResponseEntity.ok(contributionFileService.getContributionFileError(contributionId, contributionFileId).orElseThrow(
                () -> new RequestedObjectNotFoundException("Contribution file error not found"))); // to get ErrorDTO
    }
}
