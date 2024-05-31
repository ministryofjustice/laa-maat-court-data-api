package gov.uk.courtdata.eform.controller;

import gov.uk.courtdata.eform.repository.entity.EformsDecisionHistory;
import gov.uk.courtdata.eform.service.EformsDecisionHistoryService;
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
@RequestMapping("/api/eform/decision-history")
public class EformsDecisionHistoryController {

    private final EformsDecisionHistoryService eformsDecisionHistoryService;

    @GetMapping(value = "/{usn}")
    @StandardApiResponseCodes
    public List<EformsDecisionHistory> getAllEformsDecisionHistory(@PathVariable Integer usn) {
      LoggingData.USN.putInMDC(usn);
        return eformsDecisionHistoryService.getAllEformsDecisionHistory(usn);
    }

    @GetMapping(value = "/{usn}/latest-record")
    @StandardApiResponseCodes
    public EformsDecisionHistory getLatestEformsDecisionHistoryRecord(@PathVariable Integer usn) {
      LoggingData.USN.putInMDC(usn);
        return eformsDecisionHistoryService.getNewEformsDecisionHistoryRecord(usn);
    }

    @GetMapping(value = "/{usn}/previous-wrote-to-result")
    @StandardApiResponseCodes
    public EformsDecisionHistory getPreviousEformsDecisionHistoryRecordWroteToResult(@PathVariable Integer usn) {
      LoggingData.USN.putInMDC(usn);
        return eformsDecisionHistoryService.getPreviousEformsDecisionHistoryRecordWroteToResult(usn);
    }

   @PostMapping
    @StandardApiResponseCodes
    public ResponseEntity<Void> createEformsDecisionHistory(@RequestBody EformsDecisionHistory eformsDecisionHistory) {
     LoggingData.USN.putInMDC(eformsDecisionHistory.getUsn());
     LoggingData.MAAT_ID.putInMDC(eformsDecisionHistory.getRepId());
        eformsDecisionHistoryService.createEformsDecisionHistory(eformsDecisionHistory);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{usn}")
    @StandardApiResponseCodes
    public ResponseEntity<Void> deleteEformsDecisionHistory(@PathVariable Integer usn) {
      LoggingData.USN.putInMDC(usn);
        eformsDecisionHistoryService.deleteEformsDecisionHistory(usn);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{usn}")
    @StandardApiResponseCodes
    public ResponseEntity<Void> updateEformsDecisionHistoryFields(@PathVariable Integer usn,
                                                                  @RequestBody EformsDecisionHistory eformsDecisionHistory) {
      LoggingData.USN.putInMDC(usn);
      LoggingData.MAAT_ID.putInMDC(eformsDecisionHistory.getRepId());
        eformsDecisionHistoryService.updateEformsDecisionHistoryFields(usn, eformsDecisionHistory);
        return ResponseEntity.ok().build();
    }

}
