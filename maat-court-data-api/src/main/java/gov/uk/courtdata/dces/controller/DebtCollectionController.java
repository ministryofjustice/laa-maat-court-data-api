package gov.uk.courtdata.dces.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dces.service.DebtCollectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("${api-endpoints.debt-collection-enforcement}")
@Slf4j
@RequiredArgsConstructor
@XRayEnabled
@Tag(name = "Debt Collection Enforcement", description = "Rest API for Debt Collection Enforcement Service")
public class DebtCollectionController {

    private final DebtCollectionService dceService;

    @GetMapping(value = "/contributions", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get a list of XML files for contributions ")
    public List<String> getContribution(@RequestParam(name = "fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                  @RequestParam(name = "toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        log.info("Get correspondence status request for getContribution with date range from {} to {}", fromDate, toDate);
        return dceService.getContributionFiles(fromDate, toDate);
    }

    @GetMapping(value = "/final-defence-cost", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get a list of XML files for FDC (final-defence-cost).")
    public List<String> getFDC(@RequestParam(name = "fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                  @RequestParam(name = "toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        log.info("Get correspondence status request for getContribution with date range from {} to {}", fromDate, toDate);
        return dceService.getFDC(fromDate, toDate);

    }
}
