package gov.uk.courtdata.repOrder.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import gov.uk.courtdata.repOrder.service.RepOrderService;
import gov.uk.courtdata.repOrder.validator.UpdateAppDateCompletedValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@XRayEnabled
@RestController
@RequiredArgsConstructor
@Tag(name = "RepOrders", description = "Rest API for rep orders")
@RequestMapping("${api-endpoints.assessments-domain}/rep-orders")
public class RepOrderController {

    private final RepOrderService repOrderService;
    private final UpdateAppDateCompletedValidator updateAppDateCompletedValidator;

    @GetMapping(
            value = "/{repId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(description = "Retrieve a rep order record")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @ApiResponse(responseCode = "400",
            description = "Bad Request.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    @ApiResponse(responseCode = "500",
            description = "Server Error.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    public ResponseEntity<Object> getRepOrder(@PathVariable int repId) {
        log.info("Get Rep Order Request Received");
        return ResponseEntity.ok(repOrderService.find(repId));
    }


    @PostMapping(value = "/update-date-completed",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(description = "Update application date completed")
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
    public ResponseEntity<Object> updateApplicationDateCompleted(
            @Parameter(description = "Update app date",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UpdateAppDateCompleted.class)
                    )
            ) @RequestBody UpdateAppDateCompleted updateAppDateCompleted) {

        log.debug("Assessments Request Received for repId : {}", updateAppDateCompleted.getRepId());
        updateAppDateCompletedValidator.validate(updateAppDateCompleted);
        repOrderService.updateAppDateCompleted(updateAppDateCompleted);
        return ResponseEntity.ok().build();
    }
}
