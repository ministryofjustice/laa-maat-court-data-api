package gov.uk.courtdata.wqhearing.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.dto.WQHearingDTO;
import gov.uk.courtdata.wqhearing.service.WQHearingService;
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

import java.util.List;

@RestController
@RequestMapping("${api-endpoints.assessments-domain}/wq-hearing")
@Slf4j
@RequiredArgsConstructor
@XRayEnabled
@Tag(name = "WQ hearing", description = "Rest API for WQ hearing")
public class WQHearingController {

    private final WQHearingService wqHearingService;

    @GetMapping(value = "{hearingUUID}/maatId/{maatId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve WQ Hearing record")
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
    public ResponseEntity<List<WQHearingDTO>> findByMaatIdAndHearingUUID(@PathVariable int maatId, @PathVariable String hearingUUID) {
        log.info("Find WQ hearing  Request Received");
        return ResponseEntity.ok(wqHearingService.findByMaatIdAndHearingUUID(maatId, hearingUUID));
    }
}
