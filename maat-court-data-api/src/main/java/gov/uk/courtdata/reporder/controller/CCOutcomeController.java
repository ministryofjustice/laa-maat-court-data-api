package gov.uk.courtdata.reporder.controller;

import gov.uk.courtdata.annotation.StandardApiResponse;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.dto.RepOrderCCOutcomeDTO;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.RepOrderCCOutcome;
import gov.uk.courtdata.reporder.service.CCOutcomeService;
import gov.uk.courtdata.reporder.validator.CCOutComeValidationProcessor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Objects.nonNull;


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "RepOrders", description = "Rest API for RepOrder CC OutCome")
@RequestMapping("${api-endpoints.assessments-domain}/rep-orders/cc-outcome")
public class CCOutcomeController {

    private final CCOutcomeService service;

    private final CCOutComeValidationProcessor validator;


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create a new RepOrder CC outcome")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400",
            description = "Bad Request.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    @ApiResponse(responseCode = "500",
            description = "Server Error.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    public ResponseEntity<RepOrderCCOutcomeDTO> create(@Parameter(description = "RepOrder CC outcome data",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RepOrderCCOutcome.class))) @RequestBody RepOrderCCOutcome repOrderCCOutCome) {
        LoggingData.MAAT_ID.putInMDC(repOrderCCOutCome.getRepId());
        log.info("Create Financial RepOrder CC outcome");
        validator.validate(repOrderCCOutCome);
        return ResponseEntity.ok(service.create(repOrderCCOutCome));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update a RepOrder CC outcome Record")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400",
            description = "Bad Request.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    @ApiResponse(responseCode = "500",
            description = "Server Error.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    public ResponseEntity<RepOrderCCOutcomeDTO> update(@Parameter(description = "RepOrder CC outcome data",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RepOrderCCOutcome.class)
            )
    ) @RequestBody RepOrderCCOutcome repOrderCCOutCome) {
        LoggingData.MAAT_ID.putInMDC(repOrderCCOutCome.getRepId());
        log.info("Update RepOrder CC outcome  Request Received");
        validator.validate(repOrderCCOutCome);
        return ResponseEntity.ok(service.update(repOrderCCOutCome));
    }

    @GetMapping(value = "/reporder/{repId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(description = "Retrieve a RepOrder CCOutCome record")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @ApiResponse(responseCode = "400",
            description = "Bad Request.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    @ApiResponse(responseCode = "500",
            description = "Server Error.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    public ResponseEntity<List<RepOrderCCOutcomeDTO>> findByRepId(@PathVariable int repId) {
        LoggingData.MAAT_ID.putInMDC(repId);
        log.info("Find RepOrder CC Outcome Request Received");
        validator.validate(repId);
        return ResponseEntity.ok(service.findByRepId(repId));
    }

    @RequestMapping(value = "/reporder/{repId}",
            method = {RequestMethod.HEAD},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(description = "Retrieve a RepOrder CCOutCome size in the header")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400",
            description = "Bad Request.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    @ApiResponse(responseCode = "500",
            description = "Server Error.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    public ResponseEntity<List<RepOrderCCOutcomeDTO>> findByRepIdLengthInHeader(@PathVariable int repId) {
        LoggingData.MAAT_ID.putInMDC(repId);
        log.info("Find RepOrder CC Outcome Request Received");
        validator.validate(repId);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentLength(service.findByRepId(repId).size());
        return ResponseEntity.ok().headers(responseHeaders).build();
    }

    @Operation(description = "Deleting Crown Court Outcome by a RepOrder")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @DeleteMapping(value = "/rep-order/{repId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> deleteCrownCourtOutcome(@PathVariable final Integer repId) {
        if(nonNull(repId)){
            log.info("Delete CrownCourtOutcome {}", repId);
            LoggingData.MAAT_ID.putInMDC(repId);
            Integer deleteCount = service.deleteByRepId(repId);
            return ResponseEntity.ok(deleteCount);
        }else{
            log.info("repId is null");
            throw new ValidationException("repId is null");
        }
    }
}
