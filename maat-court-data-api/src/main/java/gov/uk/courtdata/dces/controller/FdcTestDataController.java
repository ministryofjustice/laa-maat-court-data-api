package gov.uk.courtdata.dces.controller;

import gov.uk.courtdata.annotation.StandardApiResponse;
import gov.uk.courtdata.dces.request.CreateFdcTestDataRequest;
import gov.uk.courtdata.dces.service.FdcContributionsTestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api-endpoints.debt-collection-enforcement-domain}")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Fdc", description = "Rest API for Final Defence Cost Test Data Generation")
@ConditionalOnProperty(prefix = "sentry", name = "environment", havingValue = "development")
public class FdcTestDataController {

    private final FdcContributionsTestService fdcContributionsTestService;

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @PostMapping(value = "/generate_prepare_fdc_data_1", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Prepare final defence cost files for processing")
    public ResponseEntity<Boolean> prepareFdcContributions(@RequestBody CreateFdcTestDataRequest request){
        log.info("Creating test data for FDC Merge 1.");
        boolean updateResult = fdcContributionsTestService.createFdcMergeTestData(request);
        log.info("Test data creation successful");
        return ResponseEntity.ok(updateResult);
    }


}