package gov.uk.courtdata.hardship.controller;

import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.hardship.service.HardshipReviewService;
import gov.uk.courtdata.hardship.validator.HardshipReviewValidationProcessor;
import gov.uk.courtdata.model.hardship.CreateHardshipReview;
import gov.uk.courtdata.model.hardship.HardshipReviewDetail;
import gov.uk.courtdata.model.hardship.UpdateHardshipReview;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Hardship Reviews", description = "Rest API for hardship reviews")
@RequestMapping("${api-endpoints.assessments-domain}/hardship")
public class HardshipReviewController {

    private final HardshipReviewService hardshipReviewService;
    private final HardshipReviewValidationProcessor validationProcessor;

    @GetMapping(value = "/{hardshipId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve a hardship review record")
    @StandardApiResponseCodes
    @ApiResponse(responseCode = "404",
            description = "Not Found.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    public ResponseEntity<HardshipReviewDTO> getHardship(
            @PathVariable int hardshipId) {
        log.info("Get Hardship Review Request Received for hardship id: {}", hardshipId);
        validationProcessor.validate(hardshipId);
        return ResponseEntity.ok(hardshipReviewService.find(hardshipId));
    }

    @GetMapping(value = "repId/{repId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve a hardship review record by repId")
    @StandardApiResponseCodes
    @ApiResponse(responseCode = "404",
            description = "Not Found.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    public ResponseEntity<HardshipReviewDTO> getHardshipByRepId(
            @PathVariable int repId) {
      LoggingData.MAAT_ID.putInMDC(repId);
        log.info("Get Hardship Review by repId = {}", repId);
        return ResponseEntity.ok(hardshipReviewService.findByRepId(repId));
    }

    @GetMapping(value = "repId/{repId}/detailType/{detailType}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve a hardship review record by repId and detail type")
    @StandardApiResponseCodes
    @ApiResponse(responseCode = "404",
            description = "Not Found.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    public ResponseEntity<List<HardshipReviewDetail>> getHardshipByDetailType(
            @PathVariable int repId,
            @PathVariable String detailType) {
      LoggingData.MAAT_ID.putInMDC(repId);
        log.info("Get Hardship Review by detail type = {} and repId = {}", detailType, repId);
        return ResponseEntity.ok(hardshipReviewService.findDetails(detailType, repId));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create a hardship review record")
    @StandardApiResponseCodes
    public ResponseEntity<HardshipReviewDTO> createHardship(
            @Parameter(description = "Hardship review data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateHardshipReview.class)
                    )
            )
            @RequestBody CreateHardshipReview hardshipReview) {
      LoggingData.MAAT_ID.putInMDC(hardshipReview.getRepId());
        log.info("Create Hardship Review Request Received");
        return ResponseEntity.ok(hardshipReviewService.create(hardshipReview));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update a hardship review record")
    @StandardApiResponseCodes
    public ResponseEntity<HardshipReviewDTO> updateHardship(
            @Parameter(description = "Hardship review data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UpdateHardshipReview.class)
                    )
            )
            @RequestBody UpdateHardshipReview hardshipReview) {
        log.info("Update Hardship Review Request Received");
        return ResponseEntity.ok(hardshipReviewService.update(hardshipReview));
    }

    @PatchMapping(value=  "/{hardshipReviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update a subset of hardship review record")
    @StandardApiResponseCodes
    public ResponseEntity<Void> updateHardship(@PathVariable int hardshipReviewId,
            @Parameter(description = "Hardship review data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
            @RequestBody Map<String, Object> updateFields) {
        log.info("Received request to update Hardship Review with id: [{}]", hardshipReviewId);
        hardshipReviewService.patch(hardshipReviewId, updateFields);
        return ResponseEntity.ok().build();
    }
}
