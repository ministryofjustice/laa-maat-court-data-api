package gov.uk.courtdata.eform.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.eform.dto.EformStagingDTO;
import gov.uk.courtdata.eform.mapper.EformStagingDTOMapper;
import gov.uk.courtdata.eform.model.EformStagingResponse;
import gov.uk.courtdata.eform.service.EformStagingService;
import gov.uk.courtdata.eform.validator.UsnValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eform/{usn}")
@Slf4j
@XRayEnabled
@RequiredArgsConstructor
public class EformStagingController {

    private static final String DEFAULT_EFORM_TYPE = "CRM14";

    private final EformStagingService eformStagingService;
    private final EformStagingDTOMapper eformStagingDTOMapper;
    private final UsnValidator usnValidator;

    @GetMapping()
    @Operation(description = "Retrieve an EFORMS_STAGING record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<EformStagingResponse> getEformApplication(@PathVariable Integer usn,
                                                                    @Parameter(description = "Used for tracing calls")
                                                                    @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        usnValidator.verifyUsnExists(usn);
        EformStagingDTO eformStagingDtoOptional = eformStagingService.retrieve(usn);

        EformStagingResponse eformStagingResponse = eformStagingDTOMapper.toEformStagingResponse(eformStagingDtoOptional);

        return ResponseEntity.ok(eformStagingResponse);
    }

    @DeleteMapping()
    @Operation(description = "Delete an EFORMS_STAGING record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Void> deleteEformApplication(@PathVariable Integer usn,
                                                       @Parameter(description = "Used for tracing calls")
                                                       @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        usnValidator.verifyUsnExists(usn);

        eformStagingService.delete(usn);

        return ResponseEntity.ok().build();
    }

    @PostMapping()
    @Operation(description = "Create an EFORMS_STAGING record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Void> createEformApplication(@PathVariable Integer usn,
                                                       @RequestParam(name = "type", required = false, defaultValue = DEFAULT_EFORM_TYPE) String type,
                                                       @RequestParam(name = "maatref", required = false) Integer maatref,
                                                       @Parameter(description = "Used for tracing calls")
                                                       @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        usnValidator.verifyUsnDoesNotExist(usn);

        EformStagingDTO eformStagingDTO = EformStagingDTO.builder()
                .usn(usn)
                .type(type)
                .maat_id(maatref)
                .build();

        eformStagingService.create(eformStagingDTO);

        return ResponseEntity.ok().build();
    }
}