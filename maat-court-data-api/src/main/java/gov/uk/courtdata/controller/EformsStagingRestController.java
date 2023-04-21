package gov.uk.courtdata.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.EformsStagingDTO;
import gov.uk.courtdata.eforms.builder.EformsStagingMapper;
import gov.uk.courtdata.eforms.service.EformsStagingService;
import gov.uk.courtdata.model.eforms.EformsApplication;
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

    private final EformsStagingMapper eformsStagingMapper;

    // TODO: this rest controller is just for testing and will replace with the Queue listener.
    @PatchMapping("/eform/{usn}")
    public ResponseEntity<Object> updateEformApplication(@PathVariable Integer usn,
                                                         @RequestBody EformsApplication eformsApplication,
                                                         @Parameter(description = "Used for tracing calls") @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        // TODO validate usn against EformsApplication usn
        EformsStagingDTO eformsStagingDTO = eformsStagingMapper.map(eformsApplication);

        eformsStagingService.execute(eformsStagingDTO);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/eform/{usn}")
    public ResponseEntity<EformsApplication> getEformsApplication(@PathVariable Integer usn,
                                                                  @Parameter(description = "Used for tracing calls") @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        eformsStagingService.retrieveEformsStagingDTO(usn);
        // TODO complete logic

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/eform/{usn}")
    public ResponseEntity<EformsApplication> deleteEformsApplication(@PathVariable Integer usn,
                                                                     @Parameter(description = "Used for tracing calls") @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        eformsStagingService.deleteEformsStagingDTO(usn);
        // TODO add logic for if not found

        return ResponseEntity.ok().build();
    }

    @PostMapping("/eform/{usn}")
    public ResponseEntity<EformsApplication> createEformsApplication(@PathVariable Integer usn,
                                                                     @RequestBody EformsApplication eformsApplication,
                                                                     @Parameter(description = "Used for tracing calls") @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        // TODO add usn validation logic
        EformsStagingDTO eformsStagingDTO = eformsStagingMapper.map(eformsApplication);

        eformsStagingService.createEformsStagingDTO(eformsStagingDTO);

        return ResponseEntity.ok().build();
    }
}