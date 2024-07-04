package gov.uk.courtdata.dces.controller;

import gov.uk.courtdata.annotation.StandardApiResponse;
import gov.uk.courtdata.dces.request.CreateFdcItemRequest;
import gov.uk.courtdata.dces.service.FdcContributionsService;
import gov.uk.courtdata.entity.FdcItemsEntity;
import gov.uk.courtdata.exception.ValidationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import static java.util.Objects.nonNull;

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
    public ResponseEntity<FdcItemsEntity> createFdcItems(@Valid @RequestBody final CreateFdcItemRequest fdcItemDTO) {
        log.info("Create FdcItems {}", fdcItemDTO);
        FdcItemsEntity fdcItem = fdcContributionsService.createFdcItems(fdcItemDTO);
        return ResponseEntity.ok(fdcItem);
    }

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @DeleteMapping(value = "/fdc-items/fdc-id/{fdcId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "")
    public ResponseEntity<Long> deleteFdcItems(@NotNull @PathVariable final Integer fdcId) {

            log.info("Delete FdcItems {}", fdcId);
            long count = fdcContributionsService.deleteFdcItems(fdcId);
            return ResponseEntity.ok(count);
    }

}