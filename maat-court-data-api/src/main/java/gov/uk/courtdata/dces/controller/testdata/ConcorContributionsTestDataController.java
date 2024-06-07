package gov.uk.courtdata.dces.controller.testdata;

import gov.uk.courtdata.annotation.NotFoundApiResponse;
import gov.uk.courtdata.dces.request.UpdateConcorContributionStatusRequest;
import gov.uk.courtdata.dces.response.ConcorContributionResponseDTO;
import gov.uk.courtdata.dces.service.ConcorContributionsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${api-endpoints.debt-collection-enforcement-domain-test-data}")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Concor Contributions Test-Data", description = "Rest API for Concor Contribution Files. This is created only to create test data for E2E")

public class ConcorContributionsTestDataController {

    private final ConcorContributionsService concorContributionsService;

    @NotFoundApiResponse
    @PutMapping(value = "/concor-contribution-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Long>> updateStatus(@Valid UpdateConcorContributionStatusRequest request){

        log.info("request {}", request);
        List<Long> updatedConcorContributionsIds = concorContributionsService.updateConcorContributionStatus(request);
        log.info("response for concor-contribution-status {}", updatedConcorContributionsIds);
        return ResponseEntity.ok(updatedConcorContributionsIds);
    }

    @NotFoundApiResponse
    @GetMapping(value = "/concor-contribution/{id}")
    public ResponseEntity<ConcorContributionResponseDTO>  getContribution(@PathVariable Integer id) {

        log.info("request {}", id);
        return ResponseEntity.ok(concorContributionsService.getConcorContribution(id));
    }
}