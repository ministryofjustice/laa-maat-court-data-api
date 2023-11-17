package gov.uk.courtdata.eform.controller;

import gov.uk.courtdata.eform.repository.entity.EformsAudit;
import gov.uk.courtdata.eform.service.EformAuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/eform/audit")
public class EformAuditController {

    private final EformAuditService eformAuditService;

    @GetMapping(value ="/{usn}")
    @StandardApiResponseCodes
    public EformsAudit getEformsAudit(@PathVariable Integer usn) {
        return eformAuditService.retrieve(usn);
    }

    @PostMapping(value ="/{usn}")
    @StandardApiResponseCodes
    public ResponseEntity<Void> createEformsAudit(@RequestBody EformsAudit eformsAudit) {
        eformAuditService.create(eformsAudit);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value ="/{usn}")
    @StandardApiResponseCodes
    public ResponseEntity<Void> deleteEformsAudit(@PathVariable Integer usn) {
        eformAuditService.delete(usn);

        return ResponseEntity.ok().build();
    }
}
