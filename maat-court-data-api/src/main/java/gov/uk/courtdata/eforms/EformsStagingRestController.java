package gov.uk.courtdata.eforms;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.fasterxml.jackson.core.JsonProcessingException;
import gov.uk.courtdata.eforms.builder.EformsStagingBuilder;
import gov.uk.courtdata.dto.EformsStagingDTO;
import gov.uk.courtdata.eforms.service.EformsStagingService;
import gov.uk.courtdata.model.eforms.EformsApplication;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api")
@Slf4j
@XRayEnabled
@RequiredArgsConstructor
public class EformsStagingRestController {

    private final EformsStagingService eformsStagingService;

    private final EformsStagingBuilder eformsStagingBuilder;


    // TODO: this rest controller is just for testing and will replace with the Queue listener.
    @PostMapping("/eform")
    public ResponseEntity<Object> processEforms(
            @RequestBody EformsApplication eformsApplication,
            @Parameter(description = "Used for tracing calls") @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId)
            throws SQLException, JsonProcessingException {

        log.info("Inside rest controller to process eforms {}", laaTransactionId);

        EformsStagingDTO eformsStagingDTO= eformsStagingBuilder.build(eformsApplication);

        eformsStagingService.execute(eformsStagingDTO);


        log.info("Finishing and returning response as OK!");
        return ResponseEntity.ok().build();
    }
}