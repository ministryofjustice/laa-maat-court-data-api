package gov.uk.courtdata.eform.controller;

import gov.uk.courtdata.eform.repository.entity.EformsHistory;
import gov.uk.courtdata.eform.service.EformsHistoryService;
import gov.uk.courtdata.enums.LoggingData;
import java.util.List;
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
@RequestMapping("/api/eform/history")
public class EformsHistoryController {

    private final EformsHistoryService eformsHistoryService;

    @GetMapping(value = "/{usn}")
    @StandardApiResponseCodes
    public List<EformsHistory> getAllEformsDecisionHistory(@PathVariable Integer usn) {
        LoggingData.USN.putInMDC(usn);
        return eformsHistoryService.getAllEformsHistory(usn);
    }

    @GetMapping(value = "/{usn}/latest-record")
    @StandardApiResponseCodes
    public EformsHistory getLatestEformsHistoryRecord(@PathVariable Integer usn) {
        LoggingData.USN.putInMDC(usn);
        return eformsHistoryService.getLatestEformsHistoryRecord(usn);
    }

    @PostMapping
    @StandardApiResponseCodes
    public ResponseEntity<Void> createEformsHistory(@RequestBody EformsHistory eformsHistory) {
        LoggingData.USN.putInMDC(eformsHistory.getUsn());
        LoggingData.MAAT_ID.putInMDC(eformsHistory.getRepId());
        eformsHistoryService.createEformsHistory(eformsHistory);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{usn}")
    @StandardApiResponseCodes
    public ResponseEntity<Void> deleteEformsHistory(@PathVariable Integer usn) {
        LoggingData.USN.putInMDC(usn);
        eformsHistoryService.deleteEformsHistory(usn);
        return ResponseEntity.ok().build();
    }
}
