package gov.uk.courtdata.iojappeal.controller;

import gov.uk.courtdata.annotation.NotFoundApiResponse;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.dto.IOJAppealDTO;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.iojappeal.service.IOJAppealService;
import gov.uk.courtdata.iojappeal.validator.IOJAppealValidationProcessor;
import gov.uk.courtdata.model.iojAppeal.CreateIOJAppeal;
import gov.uk.courtdata.model.iojAppeal.UpdateIOJAppeal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.justice.laa.crime.common.model.ioj.ApiGetIojAppealResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "ioj appeal", description = "Rest API for ioj appeal")
@RequestMapping("${api-endpoints.assessments-domain-v2}/ioj-appeal")
public class IOJAppealControllerV2 {

    private final IOJAppealService iojAppealService;

    private final IOJAppealValidationProcessor iojAppealValidationProcessor;


    //Use problemDetail remember
    @GetMapping(value = "/{legacyIojAppealId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve an IOJ Appeal record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiGetIojAppealResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<ApiGetIojAppealResponse> getIOJAppeal(@PathVariable int legacyIojAppealId) {
        log.info("Get IOJ Appeal Received: id: {}", legacyIojAppealId);
        return ResponseEntity.ok(iojAppealService.findByLegacyAppealId(legacyIojAppealId));
    }

//    @GetMapping(value = "repId/{repId}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(description = "Retrieve an IOJ Appeal record by repId")
//    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = IOJAppealDTO.class)))
//    @NotFoundApiResponse
//    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
//    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
//    public ResponseEntity<IOJAppealDTO> getIOJAppealByRepId(@PathVariable int repId,
//                                                            @Parameter(description = "Used for tracing calls")
//                                                            @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {
//        LoggingData.MAAT_ID.putInMDC(repId);
//        log.info("Get IOJ Appeal by repId: {}", repId);
//        return ResponseEntity.ok(iojAppealService.findByRepId(repId));
//    }
//
//    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(description = "Create a new Interest of Justice appeal record")
//    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = IOJAppealDTO.class)))
//    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
//    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
//    public ResponseEntity<IOJAppealDTO> createIOJAppeal(@Parameter(description = "Interest of Justice appeal data", content = @Content(mediaType = "application/json",
//            schema = @Schema(implementation = CreateIOJAppeal.class))) @Valid @RequestBody CreateIOJAppeal iojAppeal,
//                                                        @Parameter(description = "Used for tracing calls")
//                                                        @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {
//        LoggingData.MAAT_ID.putInMDC(iojAppeal.getRepId());
//        log.info("Create IOJ Appeal Request Received");
//
//        var iojAppealDTO = iojAppealService.create(iojAppeal);
//
//        return ResponseEntity.ok(iojAppealDTO);
//    }
}
