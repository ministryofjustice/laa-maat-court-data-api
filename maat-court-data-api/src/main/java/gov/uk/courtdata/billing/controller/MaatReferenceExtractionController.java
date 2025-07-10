package gov.uk.courtdata.billing.controller;

import gov.uk.courtdata.billing.service.MaatReferenceService;
import gov.uk.courtdata.eform.controller.StandardApiResponseCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Maat Reference Extraction", description = "Rest API for extracting MAAT references to send to billing teams")
@RequestMapping("${api-endpoints.billing-domain}/maat-references")
public class MaatReferenceExtractionController {
    
    private final MaatReferenceService maatReferenceService;
    
    @PostMapping
    @Operation(description = "Create MAAT reference records")
    @StandardApiResponseCodes
    public ResponseEntity<Object> populateMaatReferencesToExtract() {
        log.info("Populate MAAT references Request received");
        maatReferenceService.populateTable();
        log.info("Populate MAAT references Request completed successfully");
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @Operation(description = "Delete MAAT reference records")
    @StandardApiResponseCodes
    public ResponseEntity<Object> deleteMaatReferences() {
        log.info("Delete MAAT references Request received");
        maatReferenceService.deleteMaatReferences();
        log.info("Delete MAAT references Request completed successfully");
        return ResponseEntity.ok().build();
    }
}
