package gov.uk.courtdata.wqoffence.controller;


import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.wqoffence.service.WQOffenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "WQOffence", description = "Rest API for WQ Offence")
@RequestMapping("${api-endpoints.assessments-domain}/wq-offence")
public class WQOffenceController {

    private final WQOffenceService wqOffenceService;

    @GetMapping(value = "/{offenceId}/case/{caseId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(description = "Retrieve new offence count")
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
    public ResponseEntity<Object> getNewOffenceCount(
            @PathVariable String offenceId, @PathVariable int caseId) {
        log.info("Get new WQ offence count");
        return ResponseEntity.ok(wqOffenceService.getNewOffenceCount(caseId, offenceId));
    }
}
