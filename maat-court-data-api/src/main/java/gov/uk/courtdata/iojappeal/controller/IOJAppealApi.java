package gov.uk.courtdata.iojappeal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import uk.gov.justice.laa.crime.common.model.ioj.ApiCreateIojAppealRequest;
import uk.gov.justice.laa.crime.common.model.ioj.ApiCreateIojAppealResponse;
import uk.gov.justice.laa.crime.common.model.ioj.ApiGetIojAppealResponse;

public interface IOJAppealApi {
    @Operation(description = "Retrieve an IOJ Appeal record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiGetIojAppealResponse.class)))
    @StandardApiResponseCodes
    ResponseEntity<ApiGetIojAppealResponse> find(@PathVariable int id);


    @Operation(description = "Create a new Interest of Justice appeal record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiCreateIojAppealResponse.class)))
    @StandardApiResponseCodes
    ResponseEntity<ApiCreateIojAppealResponse> create(@Parameter(description = "Interest of Justice appeal data", content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = ApiCreateIojAppealRequest.class))) @Valid @RequestBody ApiCreateIojAppealRequest iojAppeal);
}
