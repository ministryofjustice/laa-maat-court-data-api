package gov.uk.courtdata.eform.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.eform.dto.EformStagingDTO;
import gov.uk.courtdata.eform.builder.EformApplicationMapper;
import gov.uk.courtdata.eform.model.EformApplication;
import gov.uk.courtdata.eform.service.EformStagingDAO;
import gov.uk.courtdata.eform.validator.EformApplicationUsnValidator;
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

    private final EformStagingDAO eformsStagingDAOImpl;
    private final EformApplicationMapper eformApplicationMapper;
    private final EformApplicationUsnValidator applicationUsnValidator;

    // TODO: this rest controller is just for testing and will replace with the Queue listener.
    @PatchMapping("/eform/{usn}")
    public ResponseEntity<Object> updateEformApplication(@PathVariable Integer usn,
                                                         @RequestBody EformApplication eformApplication,
                                                         @Parameter(description = "Used for tracing calls") @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        applicationUsnValidator.validate(eformApplication, usn);
        EformStagingDTO eformStagingDTO = eformApplicationMapper.map(eformApplication);
        eformsStagingDAOImpl.update(eformStagingDTO);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/eform/{usn}")
    public ResponseEntity<EformApplication> getEformsApplication(@PathVariable Integer usn,
                                                                 @Parameter(description = "Used for tracing calls") @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        // TODO complete this usnValidator.validate(usn);
        Optional<EformStagingDTO> eformsApplication = eformsStagingDAOImpl.retrieve(usn);
        // TODO complete logic, check for empty and transform

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/eform/{usn}")
    public ResponseEntity<Object> deleteEformsApplication(@PathVariable Integer usn,
                                                                     @Parameter(description = "Used for tracing calls") @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        // TODO complete this usnValidator.validate(usn);
        eformsStagingDAOImpl.delete(usn);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/eform/{usn}")
    public ResponseEntity<Object> createEformsApplication(@PathVariable Integer usn,
                                                                     @RequestBody EformApplication eformApplication,
                                                                     @Parameter(description = "Used for tracing calls") @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        // TODO add usn validation logic

        EformStagingDTO eformStagingDTO = eformApplicationMapper.map(eformApplication);

        eformsStagingDAOImpl.create(eformStagingDTO);

        return ResponseEntity.ok().build();
    }
}