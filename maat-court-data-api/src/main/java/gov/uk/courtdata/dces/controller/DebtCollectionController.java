package gov.uk.courtdata.dces.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dces.service.DebtCollectionService;
import gov.uk.courtdata.enums.LoggingData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/internal/v1/debt-collection-enforcement")
@Slf4j
@RequiredArgsConstructor
@XRayEnabled
@Tag(name = "Debt Collection Enforcement", description = "Rest API for Debt Collection Enforcement Service")
public class DebtCollectionController {

    private final DebtCollectionService dceService;

    @GetMapping(value = "/contributions", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get a list of XML files for contributions ")
    public List<String> findContributionFiles(@RequestParam(name = "fromDate") @DateTimeFormat(pattern = "dd.MM.yyyy") final LocalDate fromDate,
                                              @RequestParam(name = "toDate") @DateTimeFormat(pattern = "dd.MM.yyyy") final LocalDate toDate,
                                              @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {
        MDC.put(LoggingData.LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info("Get correspondence files for Laa-Transaction-Id {} with date range from {} to {}, ", laaTransactionId, fromDate, toDate);
        return dceService.getContributionFiles(fromDate, toDate);
    }

    @GetMapping(value = "/final-defence-cost", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get a list of XML files for FDC (final-defence-cost).")
    public List<String> getFdcFiles(@RequestParam(name = "fromDate") @DateTimeFormat(pattern = "dd.MM.yyyy") final LocalDate fromDate,
                                    @RequestParam(name = "toDate") @DateTimeFormat(pattern = "dd.MM.yyyy") final LocalDate toDate,
                                    @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        log.info("Get final-defence-cost status request for FDC with date range from {} to {}", fromDate, toDate);
        MDC.put(LoggingData.LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info("Get final-defence-cost (FDC) files for Laa-Transaction-Id {} with date range from {} to {}, ", laaTransactionId, fromDate, toDate);
        return dceService.getFdcFiles(fromDate, toDate);

    }
}
