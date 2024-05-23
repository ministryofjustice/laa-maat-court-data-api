package gov.uk.courtdata.reporder.controller;

import gov.uk.courtdata.annotation.StandardApiResponse;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.entity.RepOrderEquityEntity;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.reporder.service.RepOrderEquityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Rep Order Equity", description = "Rest API for rep order equity")
@RequestMapping("/api/internal/v1/assessment/rep-order-equity")
public class RepOrderEquityController {

    private final RepOrderEquityService repOrderEquityService;

    @GetMapping(value = "/{repOrderEquityId}")
    @Operation(description = "Retrieve a RepOrderEquity")
    @StandardApiResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "404", description = "Not Found.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public RepOrderEquityEntity getRepOrderEquity(@PathVariable Integer repOrderEquityId) {
        return repOrderEquityService.retrieve(repOrderEquityId);
    }

    @PostMapping
    @Operation(description = "Create a RepOrderEquity")
    @StandardApiResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    public ResponseEntity<Void> createRepOrderEquity(@RequestBody RepOrderEquityEntity repOrderEquityEntity) {
        LoggingData.MAAT_ID.putInMDC(repOrderEquityEntity.getRepId());
        repOrderEquityService.create(repOrderEquityEntity);

        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{repOrderEquityId}")
    @Operation(description = "Update a RepOrderEquity")
    @StandardApiResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "404", description = "Not Found.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Void> createRepOrderEquity(@PathVariable Integer repOrderEquityId,
        @RequestBody RepOrderEquityEntity repOrderEquityEntity) {
        LoggingData.MAAT_ID.putInMDC(repOrderEquityEntity.getRepId());
        repOrderEquityService.update(repOrderEquityId, repOrderEquityEntity);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{repOrderEquityId}")
    @Operation(description = "Delete a RepOrderEquity")
    @StandardApiResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    public ResponseEntity<Void> deleteRepOrderEquity(@PathVariable Integer repOrderEquityId) {
        repOrderEquityService.delete(repOrderEquityId);

        return ResponseEntity.ok().build();
    }
}
