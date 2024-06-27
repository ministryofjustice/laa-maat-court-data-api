package gov.uk.courtdata.dces.controller;

import gov.uk.courtdata.annotation.StandardApiResponse;
import gov.uk.courtdata.dces.request.CreateFdcItemRequest;
import gov.uk.courtdata.dces.request.DeleteFdcItemRequest;

import gov.uk.courtdata.dces.service.FdcContributionsService;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.exception.ValidationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static java.util.Objects.nonNull;

///api/internal/v1/debt-collection-enforcement/fdc-items

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
    public ResponseEntity<Integer> createFdcItems(@Valid @RequestBody final CreateFdcItemRequest fdcItemDTO) {
        if(nonNull(fdcItemDTO)){
            log.debug("Create FdcItems {}", fdcItemDTO);
            Optional<Integer> fdcItemId = fdcContributionsService.createFdcItems(fdcItemDTO);

            if (!fdcItemId.isPresent()) {
                log.error("Failed to create FdcItemsEntity");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            return ResponseEntity.ok(fdcItemId.get());
        }else{
            log.error("fdcItemDTO is null");
            throw new ValidationException("fdcItemDTO is null");
        }
    }

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @DeleteMapping(value = "/fdc-items/fdc-id/{fdcId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "")
    public ResponseEntity<Boolean> deleteFdcItems(@PathVariable final Integer fdcId) {
        boolean status = false;
        if(nonNull(fdcId)){
            log.info("Delete FdcItems {}", fdcId);
            status = fdcContributionsService.deleteFdcItems(fdcId);
        }
        else{
            log.info("fdcId is null");
            throw new ValidationException("fdcId is null");
        }

        return ResponseEntity.ok(status);
    }

}