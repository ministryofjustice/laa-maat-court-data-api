package gov.uk.courtdata.dces.controller.testdata;

import gov.uk.courtdata.dces.request.UpdateConcoreContributionStatusRequest;
import gov.uk.courtdata.dces.service.ConcorContributionsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${api-endpoints.debt-collection-enforcement-domain-test-data}")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Contributions", description = "Rest API for Concor Contribution Files. This is created only to create test data for E2E")

public class ConcorContributionsTestDataController {

    private final ConcorContributionsService concorContributionsService;

    @PutMapping(value = "/concor-contribution-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Long>> updateStatus (@Valid UpdateConcoreContributionStatusRequest request){

        log.info("request {}", request);
        List<Long> stringList = concorContributionsService.updateConcorContributionStatus(request);
        log.debug("response for concor-contribution-status {}", stringList);
        return ResponseEntity.ok(stringList);
    }
}