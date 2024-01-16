package gov.uk.courtdata.reporder.controller;

import gov.uk.courtdata.eform.controller.StandardApiResponseCodes;
import gov.uk.courtdata.entity.RepOrderEquityEntity;
import gov.uk.courtdata.reporder.service.RepOrderEquityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/v1/assessment/rep-order-equity")
public class RepOrderEquityController {

    private final RepOrderEquityService repOrderEquityService;

    @GetMapping(value = "/{id}")
    @StandardApiResponseCodes
    public RepOrderEquityEntity getRepOrderEquity(@PathVariable Integer id) {
        return repOrderEquityService.retrieve(id);
    }

    @PostMapping
    @StandardApiResponseCodes
    public ResponseEntity<Void> createRepOrderEquity(@RequestBody RepOrderEquityEntity repOrderEquityEntity) {
        repOrderEquityService.create(repOrderEquityEntity);

        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{id}")
    @StandardApiResponseCodes
    public ResponseEntity<Void> createRepOrderEquity(@PathVariable Integer id, @RequestBody RepOrderEquityEntity repOrderEquityEntity) {
        repOrderEquityService.update(id, repOrderEquityEntity);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    @StandardApiResponseCodes
    public ResponseEntity<Void> deleteRepOrderEquity(@PathVariable Integer id) {
        repOrderEquityService.delete(id);

        return ResponseEntity.ok().build();
    }
}
