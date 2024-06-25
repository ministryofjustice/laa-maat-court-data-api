package gov.uk.courtdata.dces.controller;

import gov.uk.courtdata.annotation.StandardApiResponse;
import gov.uk.courtdata.dces.request.CreateFdcItemRequest;
import gov.uk.courtdata.dces.request.DeleteFdcItemRequest;

import gov.uk.courtdata.dces.service.FdcContributionsService;
import gov.uk.courtdata.dto.application.FdcItemDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import uk.gov.justice.laa.crime.dto.maat.FdcItemDTO;


@RestController
@RequestMapping("${api-endpoints.debt-collection-enforcement-domain}")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Fdc", description = "Rest API for Final Defence Cost (FDC) Items")
public class FdcItemsController {

    private final FdcContributionsService fdcContributionsService;

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @PostMapping(value = "/fdc-items", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Logs that a final defence cost was processed by the Debt Recovery Company. Creates an error entry if one has been returned.")
    public ResponseEntity<Integer> createFdcItems(@RequestBody final CreateFdcItemRequest fdcItemDTO) {
        log.debug("Create FdcItems {}", fdcItemDTO);
        return ResponseEntity.ok(12122);
    }

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @DeleteMapping(value = "/fdc-items", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "")
    public ResponseEntity<Boolean> deleteFdcItems(@RequestBody final DeleteFdcItemRequest deleteFdcItemRequest) {
        log.debug("Create FdcContributionRequest {}", deleteFdcItemRequest);
        boolean response = false;
        return ResponseEntity.ok(response);
    }

}