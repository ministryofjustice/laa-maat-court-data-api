package gov.uk.courtdata.iojAppeal.controller;

import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.dto.IOJAppealDTO;
import gov.uk.courtdata.iojAppeal.service.IOJAppealService;
import gov.uk.courtdata.model.CreateIOJAppeal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static gov.uk.courtdata.enums.LoggingData.IOJ_APPEAL_ID;

@RestController
@RequestMapping("/ioj-appeal")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "ioj appeal", description = "Rest API for ioj appeal")
public class IOJAppealController {

    private final IOJAppealService iojAppealService;

    @GetMapping(value = "/{iojAppealId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve an IOJ Appeal record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = IOJAppealDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<IOJAppealDTO> getIOJAppeal(@PathVariable Integer iojAppealId) {

        log.info("Get IOJ Appeal Received: id: {}", iojAppealId);
        MDC.put(IOJ_APPEAL_ID.getValue(), String.valueOf(iojAppealId));
        return ResponseEntity.ok(iojAppealService.find(iojAppealId));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create a new Interest of Justice appeal record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = IOJAppealDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<IOJAppealDTO> createIOJAppeal(@Parameter(description = "Interest of Justice appeal data", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = CreateIOJAppeal.class))) @Valid @RequestBody CreateIOJAppeal iojAppeal) {
        log.info("Create IOJ Appeal Request Received");

        var iojAppealDTO= iojAppealService.create(iojAppeal);

        return ResponseEntity.ok(iojAppealDTO);
    }
}
