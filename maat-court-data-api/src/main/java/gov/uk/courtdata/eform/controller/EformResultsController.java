package gov.uk.courtdata.eform.controller;

import gov.uk.courtdata.eform.repository.entity.EformResultsEntity;
import gov.uk.courtdata.eform.service.EformResultsService;
import gov.uk.courtdata.enums.LoggingData;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/eform/results")
public class EformResultsController {

    private final EformResultsService eformResultsService;

    @GetMapping(value = "/{usn}")
    @StandardApiResponseCodes
    public List<EformResultsEntity> getAllEformResults(@PathVariable Integer usn) {
        LoggingData.USN.putInMDC(usn);
        return eformResultsService.getAllEformResults(usn);
    }

    @PostMapping
    @StandardApiResponseCodes
    public ResponseEntity<Void> createEformResult(@RequestBody EformResultsEntity eformResultsEntity) {
        LoggingData.MAAT_ID.putInMDC(eformResultsEntity.getMaatRef());
        LoggingData.USN.putInMDC(eformResultsEntity.getUsn());
        eformResultsService.create(eformResultsEntity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{usn}")
    @StandardApiResponseCodes
    public ResponseEntity<Void> deleteEformResult(@PathVariable Integer usn) {
        LoggingData.USN.putInMDC(usn);
        eformResultsService.delete(usn);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{usn}")
    @StandardApiResponseCodes
    public ResponseEntity<Void> updateEformResultFields(@PathVariable Integer usn, @RequestBody EformResultsEntity eformResultsEntity) {
        LoggingData.USN.putInMDC(usn);
        LoggingData.MAAT_ID.putInMDC(eformResultsEntity.getMaatRef());
        eformResultsService.updateEformResultFields(usn, eformResultsEntity);
        return ResponseEntity.ok().build();
    }

}