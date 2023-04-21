package gov.uk.courtdata.eform.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.EformsStagingDTO;
import gov.uk.courtdata.eform.builder.EformsApplicationMapper;
import gov.uk.courtdata.eform.model.EformsApplication;
import gov.uk.courtdata.eform.service.EformsStagingService;
import gov.uk.courtdata.eform.validator.EformsApplicationUsnValidator;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
@XRayEnabled
@RequiredArgsConstructor
public class EformsStagingRestController {

    private final EformsStagingService eformsStagingService;
    private final EformsApplicationMapper eformsApplicationMapper;
    private final EformsApplicationUsnValidator applicationUsnValidator;

    // TODO: this rest controller is just for testing and will replace with the Queue listener.
    @PatchMapping("/eform/{usn}")
    public ResponseEntity<Object> updateEformApplication(@PathVariable Integer usn,
                                                         @RequestBody EformsApplication eformsApplication,
                                                         @Parameter(description = "Used for tracing calls") @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        applicationUsnValidator.validate(eformsApplication, usn);
        EformsStagingDTO eformsStagingDTO = eformsApplicationMapper.map(eformsApplication);
        eformsStagingService.update(eformsStagingDTO);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/eform/{usn}")
    public ResponseEntity<EformsApplication> getEformsApplication(@PathVariable Integer usn,
                                                                  @Parameter(description = "Used for tracing calls") @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        // TODO complete this usnValidator.validate(usn);
        eformsStagingService.retrieve(usn);
        // TODO complete logic

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/eform/{usn}")
    public ResponseEntity<EformsApplication> deleteEformsApplication(@PathVariable Integer usn,
                                                                     @Parameter(description = "Used for tracing calls") @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        eformsStagingService.delete(usn);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/eform/{usn}")
    public ResponseEntity<EformsApplication> createEformsApplication(@PathVariable Integer usn,
                                                                     @RequestBody EformsApplication eformsApplication,
                                                                     @Parameter(description = "Used for tracing calls") @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        // TODO add usn validation logic

        EformsStagingDTO eformsStagingDTO = eformsApplicationMapper.map(eformsApplication);

        eformsStagingService.create(eformsStagingDTO);

        return ResponseEntity.ok().build();
    }
}