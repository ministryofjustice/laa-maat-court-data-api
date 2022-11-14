package gov.uk.courtdata.reporder.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.model.UpdateRepOrder;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import gov.uk.courtdata.reporder.service.RepOrderMvoRegService;
import gov.uk.courtdata.reporder.service.RepOrderMvoService;
import gov.uk.courtdata.reporder.service.RepOrderService;
import gov.uk.courtdata.reporder.validator.UpdateAppDateCompletedValidator;
import gov.uk.courtdata.reporder.validator.UpdateRepOrderValidator;
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

import java.util.Objects;

@Slf4j
@XRayEnabled
@RestController
@RequiredArgsConstructor
@Tag(name = "RepOrders", description = "Rest API for rep orders")
@RequestMapping("${api-endpoints.assessments-domain}/rep-orders")
public class RepOrderController {

    private final RepOrderService repOrderService;
    private final RepOrderMvoRegService repOrderMvoRegService;
    private final RepOrderMvoService repOrderMvoService;

    private final UpdateAppDateCompletedValidator updateAppDateCompletedValidator;

    private final UpdateRepOrderValidator updateRepOrderValidator;

    @GetMapping(value = "/{repId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve a rep order record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Object> getRepOrder(@PathVariable int repId) {
        log.info("Get Rep Order Request Received");
        return ResponseEntity.ok(repOrderService.find(repId));
    }


    @PostMapping(value = "/update-date-completed", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update application date completed")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Object> updateApplicationDateCompleted(@Parameter(description = "Update app date", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UpdateAppDateCompleted.class))) @RequestBody UpdateAppDateCompleted updateAppDateCompleted) {

        log.debug("Assessments Request Received for repId : {}", updateAppDateCompleted.getRepId());
        updateAppDateCompletedValidator.validate(updateAppDateCompleted);
        repOrderService.updateAppDateCompleted(updateAppDateCompleted);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/rep-order-mvo-reg/{mvoId}/current-registration", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve a rep order record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Object> getCurrentRegistrationFromRepOrderMvoReg(@PathVariable int mvoId) {
        log.info("Get Rep Order MVO Reg Request Received");
        return ResponseEntity.ok(repOrderMvoRegService.findByCurrentMvoRegistration(mvoId));
    }


    @GetMapping(value = {"/rep-order-mvo/{repId}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve a rep order record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Object> getRepOrderMvoByRepIdAndVehicleOwner(@PathVariable int repId, @RequestParam(value = "owner",   required = false) String vehicleOwner) {
        log.info("Get Rep Order MVO Request Received");
        return ResponseEntity.ok(repOrderMvoService.findRepOrderMvoByRepIdAndVehicleOwner(repId, Objects.requireNonNullElse(vehicleOwner, "N")));

    }


    @PutMapping
    @Operation(description = "Update a rep order record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Object> updateRepOrder(@Parameter(description = "Update a rep order record", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UpdateRepOrder.class))) @RequestBody UpdateRepOrder updateRepOrder) {
        log.debug("Assessments Request Received for repId : {}", updateRepOrder.getRepId());
        updateRepOrderValidator.validate(updateRepOrder);
        repOrderService.updateRepOrder(updateRepOrder);
        return ResponseEntity.ok().build();
    }

}
