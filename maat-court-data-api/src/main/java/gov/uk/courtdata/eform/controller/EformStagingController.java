package gov.uk.courtdata.eform.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.eform.builder.EformApplicationMapper;
import gov.uk.courtdata.eform.dto.EformStagingDTO;
import gov.uk.courtdata.eform.model.EformStagingResponse;
import gov.uk.courtdata.eform.service.EformStagingDAO;
import gov.uk.courtdata.eform.validator.TypeValidator;
import gov.uk.courtdata.eform.validator.UsnValidator;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@Slf4j
@XRayEnabled
@RequiredArgsConstructor
public class EformStagingController {

    private static final String DEFAULT_EFORM_TYPE = "CRM14";

    private final EformStagingDAO eformsStagingDAOImpl;
    private final EformApplicationMapper eformApplicationMapper;
    private final UsnValidator usnValidator;
    private final TypeValidator typeValidator;

    @PatchMapping("/eform/{usn}")
    public ResponseEntity<Object> updateEformApplication(@PathVariable Integer usn,
                                                         @RequestParam(name = "type", required = false, value = DEFAULT_EFORM_TYPE) String type,
                                                         @Parameter(description = "Used for tracing calls") @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        usnValidator.validate(usn);
        typeValidator.validate(type);

        EformStagingDTO eformStagingDTO = gov.uk.courtdata.eform.dto.EformStagingDTO.builder()
                .usn(usn)
                .type(type)
                .build();

        eformsStagingDAOImpl.update(eformStagingDTO);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/eform/{usn}")
    public ResponseEntity<EformStagingResponse> getEformsApplication(@PathVariable Integer usn,
                                                                     @Parameter(description = "Used for tracing calls") @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        usnValidator.validate(usn);
        Optional<EformStagingDTO> eformsApplication = eformsStagingDAOImpl.retrieve(usn);

        // TODO need to map this from entity
        EformStagingResponse eformStagingResponse = new EformStagingResponse();

        return ResponseEntity.ok(eformStagingResponse);
    }

    @DeleteMapping("/eform/{usn}")
    public ResponseEntity<Object> deleteEformsApplication(@PathVariable Integer usn,
                                                          @Parameter(description = "Used for tracing calls") @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        usnValidator.validate(usn);

        eformsStagingDAOImpl.delete(usn);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/eform/{usn}")
    public ResponseEntity<Object> createEformsApplication(@PathVariable Integer usn,
                                                          @RequestParam(name = "type", required = false, value = DEFAULT_EFORM_TYPE) String type,
                                                          @Parameter(description = "Used for tracing calls") @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        usnValidator.validate(usn);
        typeValidator.validate(type);

        EformStagingDTO eformStagingDTO = gov.uk.courtdata.eform.dto.EformStagingDTO.builder()
                .usn(usn)
                .type(type)
                .build();

        eformsStagingDAOImpl.create(eformStagingDTO);

        return ResponseEntity.ok().build();
    }
}