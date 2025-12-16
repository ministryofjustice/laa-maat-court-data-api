package gov.uk.courtdata.iojappeal.controller;

import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.iojappeal.service.IOJAppealV2Service;
import gov.uk.courtdata.iojappeal.validator.ApiCreateIojAppealRequestValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.justice.laa.crime.common.model.ioj.ApiCreateIojAppealRequest;
import uk.gov.justice.laa.crime.common.model.ioj.ApiCreateIojAppealResponse;
import uk.gov.justice.laa.crime.common.model.ioj.ApiGetIojAppealResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "ioj appeal", description = "Rest API for ioj appeal")
@RequestMapping("${api-endpoints.assessments-domain-v2}/ioj-appeals")
public class IOJAppealControllerV2 {

    private final IOJAppealV2Service iojAppealService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve an IOJ Appeal record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiGetIojAppealResponse.class)))
    @StandardApiResponseCodes
    public ResponseEntity<ApiGetIojAppealResponse> find(@PathVariable int id) {
        log.info("Get IOJ Appeal Received: id: {}", id);
        return ResponseEntity.ok(iojAppealService.find(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create a new Interest of Justice appeal record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiCreateIojAppealResponse.class)))
    @StandardApiResponseCodes
    public ResponseEntity<ApiCreateIojAppealResponse> create(@Parameter(description = "Interest of Justice appeal data", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ApiCreateIojAppealRequest.class))) @Valid @RequestBody ApiCreateIojAppealRequest iojAppeal) {
        LoggingData.MAAT_ID.putInMDC(iojAppeal.getIojAppealMetadata().getLegacyApplicationId());
        log.info("Create IOJ Appeal Request Received");

        List<String> validationErrors = ApiCreateIojAppealRequestValidator.validateRequest(iojAppeal);
        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Unable to create IoJ Appeal: " + validationErrors);
        }

        ApiCreateIojAppealResponse apiCreateIojAppealResponse = iojAppealService.create(iojAppeal);

        return ResponseEntity.ok(apiCreateIojAppealResponse);
    }
}
