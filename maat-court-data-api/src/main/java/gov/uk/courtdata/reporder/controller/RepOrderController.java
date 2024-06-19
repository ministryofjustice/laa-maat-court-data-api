package gov.uk.courtdata.reporder.controller;

import gov.uk.courtdata.annotation.NotFoundApiResponse;
import gov.uk.courtdata.annotation.StandardApiResponse;
import gov.uk.courtdata.applicant.controller.StandardApiResponseCodes;
import gov.uk.courtdata.dto.AssessorDetails;
import gov.uk.courtdata.dto.RepOrderDTO;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.model.CreateRepOrder;
import gov.uk.courtdata.model.UpdateRepOrder;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import gov.uk.courtdata.reporder.service.RepOrderMvoRegService;
import gov.uk.courtdata.reporder.service.RepOrderMvoService;
import gov.uk.courtdata.reporder.service.RepOrderService;
import gov.uk.courtdata.reporder.validator.UpdateAppDateCompletedValidator;
import gov.uk.courtdata.repository.RepOrderRepository;
import gov.uk.courtdata.validator.MaatIdValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "RepOrders", description = "Rest API for rep orders")
@RequestMapping("${api-endpoints.assessments-domain}/rep-orders")
public class RepOrderController {

    private final RepOrderService repOrderService;
    private final RepOrderMvoService repOrderMvoService;
    private final MaatIdValidator maatIdValidator;
    private final RepOrderMvoRegService repOrderMvoRegService;
    private final UpdateAppDateCompletedValidator updateAppDateCompletedValidator;
    private final RepOrderRepository repOrderRepository;

    @RequestMapping(value = "/{repId}",
            method = {RequestMethod.GET, RequestMethod.HEAD},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(description = "Retrieve a rep order record")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @StandardApiResponse
    public ResponseEntity<Object> find(HttpServletRequest request, @PathVariable int repId,
                                       @RequestParam(value = "has_sentence_order_date", defaultValue = "false")
                                       boolean hasSentenceOrderDate) {
        LoggingData.MAAT_ID.putInMDC(repId);
        log.info("Get Rep Order Request Received");
        if (request.getMethod().equals(RequestMethod.HEAD.name())) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentLength(repOrderService.exists(repId) ? 1 : 0);
            return ResponseEntity.ok().headers(responseHeaders).build();
        }
        return ResponseEntity.ok(repOrderService.find(repId, hasSentenceOrderDate));
    }

    @PostMapping(value = "/update-date-completed", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update application date completed")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UpdateAppDateCompleted.class)
            )
    )
    @StandardApiResponse
    public ResponseEntity<RepOrderDTO> updateApplicationDateCompleted(@RequestBody UpdateAppDateCompleted updateAppDateCompleted) {
        LoggingData.MAAT_ID.putInMDC(updateAppDateCompleted.getRepId());
        log.debug("Assessments Request Received for repId : {}", updateAppDateCompleted.getRepId());
        updateAppDateCompletedValidator.validate(updateAppDateCompleted);
        return ResponseEntity.ok(repOrderService.updateDateCompleted(updateAppDateCompleted));
    }

    @GetMapping(value = "/rep-order-usn/{usn}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve rep order records by USN")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @StandardApiResponse
    public ResponseEntity<Object> findByUsn(@PathVariable int usn) {
        log.debug("Get Rep Order By USN Received");
        List<RepOrderEntity> repOrderEntityList = repOrderRepository.findByUsn(usn);
        return ResponseEntity.ok(repOrderEntityList.stream().map(RepOrderEntity::getId).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/rep-order-mvo-reg/{mvoId}/current-registration", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve a rep order record")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @StandardApiResponse
    public ResponseEntity<Object> findByCurrentRegistration(@PathVariable int mvoId) {
        log.info("Get Rep Order MVO Reg Request Received");
        return ResponseEntity.ok(repOrderMvoRegService.findByCurrentMvoRegistration(mvoId));
    }


    @GetMapping(value = {"/rep-order-mvo/{repId}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve a rep order record")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @StandardApiResponse
    public ResponseEntity<Object> findByRepIdAndVehicleOwner(@PathVariable int repId,
                                                             @RequestParam(value = "owner", required = false)
                                                             String vehicleOwner) {
        LoggingData.MAAT_ID.putInMDC(repId);
        log.info("Get Rep Order MVO Request Received");
        return ResponseEntity.ok(repOrderMvoService.findRepOrderMvoByRepIdAndVehicleOwner(
                repId, Objects.requireNonNullElse(vehicleOwner, "N")
        ));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create a rep order record")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CreateRepOrder.class)
            )
    )
    @StandardApiResponse
    public ResponseEntity<RepOrderDTO> create(@Valid @RequestBody CreateRepOrder createRepOrder) {
        LoggingData.USN.putInMDC(createRepOrder.getUsn());
        log.debug("Create Rep order request");
        return ResponseEntity.ok(repOrderService.create(createRepOrder));
    }

    @PutMapping
    @Operation(description = "Update a rep order record")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UpdateRepOrder.class)
            )
    )
    @StandardApiResponse
    public ResponseEntity<RepOrderDTO> update(@RequestBody UpdateRepOrder updateRepOrder) {
        LoggingData.MAAT_ID.putInMDC(updateRepOrder.getRepId());
        LoggingData.USN.putInMDC(updateRepOrder.getUsn());
        log.debug("Update Rep order request received for repId : {}", updateRepOrder.getRepId());
        maatIdValidator.validate(updateRepOrder.getRepId());
        return ResponseEntity.ok(repOrderService.update(updateRepOrder));
    }

    @DeleteMapping(value = "/{repId}")
    @Operation(description = "Delete a rep order record")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
            )
    )
    @StandardApiResponse
    public ResponseEntity<RepOrderDTO> delete(@PathVariable Integer repId) {
        LoggingData.MAAT_ID.putInMDC(repId);
        log.debug("Delete Rep order request");
        repOrderService.delete(repId);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{repId}/ioj-assessor-details",
            method = {RequestMethod.GET},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(description = "Retrieve details of the interests of justice assessor for a given representation order")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @StandardApiResponse
    public ResponseEntity<AssessorDetails> findIOJAssessorDetails(@PathVariable int repId) {
        LoggingData.MAAT_ID.putInMDC(repId);
        AssessorDetails iojAssessorDetails = repOrderService.findIOJAssessorDetails(repId);
        return ResponseEntity.ok(iojAssessorDetails);
    }


  @PatchMapping(value = "/{repId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Partial Update of a Rep record")
    @StandardApiResponseCodes
    @NotFoundApiResponse
  public ResponseEntity<Void> updateRepOrder(@PathVariable int repId,
      @RequestBody Map<String, Object> updatedFields) {
    LoggingData.MAAT_ID.putInMDC(repId);
        log.info("Partial Update of Rep Order Request Received");
    repOrderService.update(repId, updatedFields);
        return ResponseEntity.ok().build();
    }
}
