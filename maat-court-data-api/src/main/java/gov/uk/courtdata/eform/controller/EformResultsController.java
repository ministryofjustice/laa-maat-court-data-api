package gov.uk.courtdata.eform.controller;

import gov.uk.courtdata.eform.repository.entity.EformResultsEntity;
import gov.uk.courtdata.eform.service.EformResultsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/eform/results")
public class EformResultsController {

    private final EformResultsService eformResultsService;

    @GetMapping(value = "/{usn}")
    @StandardApiResponseCodes
    public EformResultsEntity getEformResult(@PathVariable Integer usn) {
        return eformResultsService.retrieve(usn);
    }

    @PostMapping
    @StandardApiResponseCodes
    public ResponseEntity<Void> createEformResult(@RequestBody EformResultsEntity eformResultsEntity) {
        eformResultsService.create(eformResultsEntity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{usn}")
    @StandardApiResponseCodes
    public ResponseEntity<Void> deleteEformResult(@PathVariable Integer usn) {
        eformResultsService.delete(usn);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{usn}")
    @StandardApiResponseCodes
    public ResponseEntity<Void> updateEformResultFields(@PathVariable Integer usn, @RequestBody EformResultsEntity eformResultsEntity) {
        eformResultsService.updateEformResultFields(usn, eformResultsEntity);
        return ResponseEntity.ok().build();
    }

}