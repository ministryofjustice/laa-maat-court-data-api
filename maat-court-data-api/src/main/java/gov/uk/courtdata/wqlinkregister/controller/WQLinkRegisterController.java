package gov.uk.courtdata.wqlinkregister.controller;

import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.dto.WQLinkRegisterDTO;
import gov.uk.courtdata.wqlinkregister.service.WQLinkRegisterService;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "WQ Link Register", description = "Rest API for WQ Link Register")
@RequestMapping("${api-endpoints.assessments-domain}/wq-link-register")
public class WQLinkRegisterController {

    private final WQLinkRegisterService wqLinkRegisterService;

    @GetMapping(value = "/{maatID}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve WQ Link Register record")
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
    public ResponseEntity<List<WQLinkRegisterDTO>> findOffenceByMaatId(@PathVariable int maatID) {
        log.info("Find WQ Link Register by  maat id Request Received");
        return ResponseEntity.ok(wqLinkRegisterService.findByMaatId(maatID));
    }
}
