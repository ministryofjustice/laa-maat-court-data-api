package gov.uk.courtdata.eform.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.eform.dto.EformStagingDTO;
import gov.uk.courtdata.eform.mapper.EformStagingDTOMapper;
import gov.uk.courtdata.eform.model.EformStagingResponse;
import gov.uk.courtdata.eform.service.EformStagingService;
import gov.uk.courtdata.eform.validator.UsnValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eform")
@Slf4j
@XRayEnabled
@RequiredArgsConstructor
public class EformStagingController {

    private static final String DEFAULT_EFORM_TYPE = "CRM14";

    private final EformStagingService eformStagingService;
    private final EformStagingDTOMapper eformStagingDTOMapper;
    private final UsnValidator usnValidator;

    @GetMapping(value ="/{usn}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve an EFORMS_STAGING record")
    @StandardApiResponseCodes
    public ResponseEntity<EformStagingResponse> getEformApplication(@PathVariable Integer usn,
                                                                    @Parameter(description = "Used for tracing calls")
                                                                    @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        EformStagingDTO eformStagingDTO = eformStagingService.retrieve(usn);

        EformStagingResponse eformStagingResponse = eformStagingDTOMapper.toEformStagingResponse(eformStagingDTO);

        return ResponseEntity.ok(eformStagingResponse);
    }

    @DeleteMapping(value = "/{usn}")
    @Operation(description = "Delete an EFORMS_STAGING record")
    @StandardApiResponseCodes
    public ResponseEntity<Void> deleteEformApplication(@PathVariable Integer usn,
                                                       @Parameter(description = "Used for tracing calls")
                                                       @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        usnValidator.verifyUsnExists(usn);

        eformStagingService.delete(usn);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value ="/{usn}", consumes = MediaType.APPLICATION_XML_VALUE)
    @Operation(description = "Create an EFORMS_STAGING record")
    @StandardApiResponseCodes
    public ResponseEntity<Void> createEformApplication(@PathVariable Integer usn,
                                                       @RequestParam(name = "type", required = false, defaultValue = DEFAULT_EFORM_TYPE) String type,
                                                       @RequestParam(name = "maatRef", required = false) Integer maatRef,
                                                       @RequestParam(name = "userCreated", required = false) String userCreated,
                                                       @RequestBody String xmlDoc,
                                                       @Parameter(description = "Used for tracing calls")
                                                       @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        usnValidator.verifyUsnDoesNotExist(usn);

        EformStagingDTO eformStagingDTO = EformStagingDTO.builder()
                .usn(usn)
                .type(type)
                .maatRef(maatRef)
                .userCreated(userCreated)
                .xmlDoc(xmlDoc)
                .build();

        eformStagingService.create(eformStagingDTO);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/initialise/{usn}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create an dummy/placeholder EFORMS_STAGING record for Crime Apply")
    @StandardApiResponseCodes
    public ResponseEntity<EformStagingResponse> retriveOrInsertDummyUsnRecord(@PathVariable Integer usn) {

        EformStagingDTO eformStagingDto = eformStagingService.createOrRetrieve(usn);

        EformStagingResponse eformStagingResponse = eformStagingDTOMapper.toEformStagingResponse(eformStagingDto);

        return ResponseEntity.ok(eformStagingResponse);
    }
}