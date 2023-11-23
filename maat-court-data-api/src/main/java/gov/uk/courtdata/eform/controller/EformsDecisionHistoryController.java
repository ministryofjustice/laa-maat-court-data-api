package gov.uk.courtdata.eform.controller;

import gov.uk.courtdata.eform.repository.entity.EformsDecisionHistory;
import gov.uk.courtdata.eform.service.EformsDecisionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/eform/decision-history")
public class EformsDecisionHistoryController {

    private final EformsDecisionHistoryService eformsDecisionHistoryService;

    @GetMapping(value = "/{usn}")
    @StandardApiResponseCodes
    public List<EformsDecisionHistory> getAllEformsDecisionHistory(@PathVariable Integer usn) {
        return eformsDecisionHistoryService.getAllEformsDecisionHistory(usn);
    }

    @GetMapping(value = "/{usn}/latest-record")
    @StandardApiResponseCodes
    public EformsDecisionHistory getLatestEformsDecisionHistoryRecord(@PathVariable Integer usn) {
        return eformsDecisionHistoryService.getNewEformsDecisionHistoryRecord(usn);
    }

    @GetMapping(value = "/{usn}/previous-wrote-to-result")
    @StandardApiResponseCodes
    public EformsDecisionHistory getPreviousEformsDecisionHistoryRecordWroteToResult(@PathVariable Integer usn) {
        return eformsDecisionHistoryService.getPreviousEformsDecisionHistoryRecordWroteToResult(usn);
    }

   @PostMapping
    @StandardApiResponseCodes
    public ResponseEntity<Void> createEformsDecisionHistory(@RequestBody EformsDecisionHistory eformsDecisionHistory) {
        eformsDecisionHistoryService.createEformsDecisionHistory(eformsDecisionHistory);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{usn}")
    @StandardApiResponseCodes
    public ResponseEntity<Void> deleteEformsDecisionHistory(@PathVariable Integer usn) {
        eformsDecisionHistoryService.deleteEformsDecisionHistory(usn);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{usn}")
    @StandardApiResponseCodes
    public ResponseEntity<Void> updateEformsDecisionHistoryFields(@PathVariable Integer usn,
                                                                  @RequestBody EformsDecisionHistory eformsDecisionHistory) {
        eformsDecisionHistoryService.updateEformsDecisionHistoryFields(usn, eformsDecisionHistory);
        return ResponseEntity.ok().build();
    }

}
