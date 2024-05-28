package gov.uk.courtdata.eform.controller;

import gov.uk.courtdata.eform.repository.entity.EformsAudit;
import gov.uk.courtdata.eform.service.EformAuditService;
import gov.uk.courtdata.enums.LoggingData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/eform/audit")
public class EformAuditController {

    private final EformAuditService eformAuditService;

    @GetMapping(value ="/{usn}")
    @StandardApiResponseCodes
    public EformsAudit getEformsAudit(@PathVariable Integer usn) {
      LoggingData.USN.putInMDC(usn);
        return eformAuditService.retrieve(usn);
    }

    @PostMapping
    @StandardApiResponseCodes
    public ResponseEntity<Void> createEformsAudit(@RequestBody EformsAudit eformsAudit) {
      LoggingData.USN.putInMDC(eformsAudit.getUsn());
      LoggingData.MAAT_ID.putInMDC(eformsAudit.getMaatRef());
        eformAuditService.create(eformsAudit);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value ="/{usn}")
    @StandardApiResponseCodes
    public ResponseEntity<Void> deleteEformsAudit(@PathVariable Integer usn) {
      LoggingData.USN.putInMDC(usn);
        eformAuditService.delete(usn);

        return ResponseEntity.ok().build();
    }
}
