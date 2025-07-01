package gov.uk.courtdata.billing.controller;

import gov.uk.courtdata.billing.service.MaatReferenceService;
import gov.uk.courtdata.eform.controller.StandardApiResponseCodes;
import gov.uk.courtdata.exception.RecordsAlreadyExistException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Maat Reference Extraction", description = "Rest API for extracting MAAT references to send to billing teams")
@RequestMapping("${api-endpoints.billing-domain}")
public class MaatReferenceExtractionController {
    
    private final MaatReferenceService maatReferenceService;
    
    @PostMapping(value = "/populate-maat-references")
    @Operation(description = "Populate the MAAT_REFS_TO_EXTRACT table with data from REP_ORDERS")
    @StandardApiResponseCodes
    public ResponseEntity<Object> populateMaatReferencesToExtract() {
        log.info("Populate MAAT references Request received");
        
        try {
            maatReferenceService.populateTable();
            log.info("Populate MAAT references Request completed successfully");
            return ResponseEntity.ok().build();
        } catch (RecordsAlreadyExistException ex) {
            log.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
