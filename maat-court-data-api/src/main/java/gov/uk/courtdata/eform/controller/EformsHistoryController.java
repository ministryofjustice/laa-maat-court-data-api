package gov.uk.courtdata.eform.controller;

import gov.uk.courtdata.eform.repository.entity.EformsHistory;
import gov.uk.courtdata.eform.service.EformsHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/eform/history")
public class EformsHistoryController {

    private final EformsHistoryService eformsHistoryService;

    @GetMapping(value = "/{usn}")
    @StandardApiResponseCodes
    public List<EformsHistory> getAllEformsDecisionHistory(@PathVariable Integer usn) {
        return eformsHistoryService.getAllEformsHistory(usn);
    }

    @GetMapping(value = "/{usn}/latest-record")
    @StandardApiResponseCodes
    public EformsHistory getLatestEformsHistoryRecord(@PathVariable Integer usn) {
        return eformsHistoryService.getLatestEformsHistoryRecord(usn);
    }

    @PostMapping
    @StandardApiResponseCodes
    public ResponseEntity<Void> createEformsHistory(@RequestBody EformsHistory eformsHistory) {
        eformsHistoryService.createEformsHistory(eformsHistory);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{usn}")
    @StandardApiResponseCodes
    public ResponseEntity<Void> deleteEformsHistory(@PathVariable Integer usn) {
        eformsHistoryService.deleteEformsHistory(usn);
        return ResponseEntity.ok().build();
    }
}
