package gov.uk.courtdata.billing.controller;

import gov.uk.courtdata.billing.dto.RepOrderBillingDTO;
import gov.uk.courtdata.billing.service.RepOrderBillingService;
import gov.uk.courtdata.annotation.StandardApiResponseCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api-endpoints.billing-domain}/rep-orders")
@Tag(name = "RepOrder", description = "Rest API for billing-specific rep orders")
public class RepOrderBillingController {

    private final RepOrderBillingService repOrderBillingService;

    @GetMapping
    @Operation(description = "Retrieve rep orders for billing")
    @StandardApiResponseCodes
    public ResponseEntity<List<RepOrderBillingDTO>> getRepOrders() {
        return ResponseEntity.ok(repOrderBillingService.getRepOrdersForBilling());
    }
}
