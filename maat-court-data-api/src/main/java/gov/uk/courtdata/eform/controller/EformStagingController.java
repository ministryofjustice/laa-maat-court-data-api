package gov.uk.courtdata.eform.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.eform.dto.EformStagingDTO;
import gov.uk.courtdata.eform.mapper.EformStagingDTOMapper;
import gov.uk.courtdata.eform.model.EformStagingResponse;
import gov.uk.courtdata.eform.service.EformStagingDAO;
import gov.uk.courtdata.eform.validator.TypeValidator;
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
@RequestMapping("/api")
@Slf4j
@XRayEnabled
@RequiredArgsConstructor
public class EformStagingController {

    private static final String DEFAULT_EFORM_TYPE = "CRM14";

    private final EformStagingDAO eformStagingDAO;
    private final EformStagingDTOMapper eformStagingDTOMapper;
    private final UsnValidator usnValidator;
    private final TypeValidator typeValidator;

    @PatchMapping("/eform/{usn}")
    @Operation(description = "Update an EFORMS_STAGING record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Void> updateEformApplication(@PathVariable Integer usn,
                                                       @RequestParam(name = "type", required = false, defaultValue = DEFAULT_EFORM_TYPE) String type,
                                                       @Parameter(description = "Used for tracing calls")
                                                       @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        //USN Should Already be in DB
        //Validation will throw an error only when not in DB
        usnValidator.verifyUsnExists(usn);
        //This will pass on
        typeValidator.validate(type);

        EformStagingDTO eformStagingDTO = gov.uk.courtdata.eform.dto.EformStagingDTO.builder()
                .usn(usn)
                .type(type)
                .build();

        eformStagingDAO.update(eformStagingDTO);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/eform/{usn}")
    @Operation(description = "Retrieve an EFORMS_STAGING record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<EformStagingResponse> getEformApplication(@PathVariable Integer usn,
                                                                    @Parameter(description = "Used for tracing calls")
                                                                    @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        usnValidator.verifyUsnExists(usn);
        EformStagingDTO eformStagingDtoOptional = eformStagingDAO.retrieve(usn);

        EformStagingResponse eformStagingResponse = eformStagingDTOMapper.toEformStagingResponse(eformStagingDtoOptional);

        return ResponseEntity.ok(eformStagingResponse);
    }

    @DeleteMapping("/eform/{usn}")
    @Operation(description = "Delete an EFORMS_STAGING record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Void> deleteEformApplication(@PathVariable Integer usn,
                                                       @Parameter(description = "Used for tracing calls")
                                                       @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        usnValidator.verifyUsnExists(usn);

        eformStagingDAO.delete(usn);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/eform/{usn}")
    @Operation(description = "Create an EFORMS_STAGING record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Void> createEformApplication(@PathVariable Integer usn,
                                                       @RequestParam(name = "type", required = false, defaultValue = DEFAULT_EFORM_TYPE) String type,
                                                       @Parameter(description = "Used for tracing calls")
                                                       @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        usnValidator.verifyUsnDoesNotExist(usn);
        //This will pass fail/stop here
        typeValidator.validate(type);

        EformStagingDTO eformStagingDTO = gov.uk.courtdata.eform.dto.EformStagingDTO.builder()
                .usn(usn)
                .type(type)
                .build();

        eformStagingDAO.create(eformStagingDTO);

        return ResponseEntity.ok().build();
    }
}