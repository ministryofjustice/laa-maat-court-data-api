package gov.uk.courtdata.dces.controller;

import gov.uk.courtdata.dces.service.DebtCollectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Debt Collection Enforcement", description = "Rest API for Debt Collection Enforcement Service")
@RequestMapping("${api-endpoints.debt-collection-enforcement-domain}")
public class DebtCollectionController {

    private final DebtCollectionService debtCollectionService;

    /*
    The naming is misleading, these both get CONTRIBUTION_FILES xml, not actual fdc/contributions. This is used by the ReportService.
    These will be removed once the new endpoints in the ContributionFileController have been verified.
     */

    /**
     * @deprecated Being moved to the ContributionFileController, with associated change of endpoint uri.
     * Returns a list of XML from all Concor Contribution Files in specified date range.
     * @param fromDate End date for date range of Contribution Files
     * @param toDate End date for date range of Contribution Files
     * @param laaTransactionId TransactionID for following through applications
     * @return
     */
    @Deprecated(forRemoval = true)
    @GetMapping(value = "/contributions", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get a list of XML files for contributions ")
    public List<String> findContributionFiles(@RequestParam(name = "fromDate") @DateTimeFormat(pattern = "dd.MM.yyyy") final LocalDate fromDate,
                                              @RequestParam(name = "toDate") @DateTimeFormat(pattern = "dd.MM.yyyy") final LocalDate toDate,
                                              @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {
        log.info("Get correspondence files for Laa-Transaction-Id {} with date range from {} to {}, ", laaTransactionId, fromDate, toDate);
        return debtCollectionService.getContributionFiles(fromDate, toDate);
    }

    /**
     * @deprecated Being moved to the ContributionFileController, with associated change of endpoint uri.
     * Returns a list of XML from all FDC ( Final Defence Cost ) Contribution Files in specified date range.
     * @param fromDate End date for date range of Contribution Files
     * @param toDate End date for date range of Contribution Files
     * @param laaTransactionId TransactionID for following through applications
     * @return
     */
    @Deprecated(forRemoval = true)
    @GetMapping(value = "/final-defence-cost", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get a list of XML files for FDC (final-defence-cost).")
    public List<String> getFdcFiles(@RequestParam(name = "fromDate") @DateTimeFormat(pattern = "dd.MM.yyyy") final LocalDate fromDate,
                                    @RequestParam(name = "toDate") @DateTimeFormat(pattern = "dd.MM.yyyy") final LocalDate toDate,
                                    @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        log.info("Get final-defence-cost status request for FDC with date range from {} to {}", fromDate, toDate);
        log.info("Get final-defence-cost (FDC) files for Laa-Transaction-Id {} with date range from {} to {}, ", laaTransactionId, fromDate, toDate);
        return debtCollectionService.getFdcFiles(fromDate, toDate);
    }
}
