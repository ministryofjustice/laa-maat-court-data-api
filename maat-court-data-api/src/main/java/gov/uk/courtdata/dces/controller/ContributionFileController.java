package gov.uk.courtdata.dces.controller;

import gov.uk.courtdata.dces.response.ContributionFileErrorResponse;
import gov.uk.courtdata.dces.response.ContributionFileResponse;
import gov.uk.courtdata.dces.service.ContributionFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api-endpoints.debt-collection-enforcement-domain}/contribution-file")
@Slf4j
@RequiredArgsConstructor
public class ContributionFileController {
    private final ContributionFileService contributionFileService;

    @GetMapping("/{contributionFileId}")
    public ResponseEntity<ContributionFileResponse> getContributionFile(@PathVariable int contributionFileId) {
        return ResponseEntity.of(contributionFileService.getContributionFile(contributionFileId));
    }

    @GetMapping("/{contributionFileId}/error")
    public ResponseEntity<List<ContributionFileErrorResponse>> getAllContributionFileError(@PathVariable int contributionFileId) {
        return ResponseEntity.ok(contributionFileService.getAllContributionFileError(contributionFileId));
    }

    @GetMapping("/{contributionFileId}/error/{contributionId}")
    public ResponseEntity<ContributionFileErrorResponse> getContributionFileError(@PathVariable int contributionFileId,
                                                                                  @PathVariable int contributionId) {
        return ResponseEntity.of(contributionFileService.getContributionFileError(contributionId, contributionFileId));
    }
}
