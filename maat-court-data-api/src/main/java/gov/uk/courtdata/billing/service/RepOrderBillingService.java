package gov.uk.courtdata.billing.service;

import gov.uk.courtdata.billing.dto.RepOrderBillingDTO;
import gov.uk.courtdata.billing.entity.RepOrderBillingEntity;
import gov.uk.courtdata.billing.mapper.RepOrderBillingMapper;
import gov.uk.courtdata.billing.request.UpdateBillingRequest;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.billing.repository.RepOrderBillingRepository;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepOrderBillingService {

    private final RepOrderBillingRepository repOrderBillingRepository;

    public List<RepOrderBillingDTO> getRepOrdersForBilling() {
        List<RepOrderBillingEntity> extractedRepOrders = repOrderBillingRepository.getRepOrdersForBilling();

        if (extractedRepOrders.isEmpty()) {
            return Collections.emptyList();
        }

        return extractedRepOrders.stream()
            .map(RepOrderBillingMapper::mapEntityToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = MAATCourtDataException.class)
    public void resetRepOrdersSentForBilling(UpdateBillingRequest request) {
        int updatedRows = repOrderBillingRepository.resetBillingFlagForRepOrderIds(
            request.getUserModified(), request.getIds());

        if (updatedRows != request.getIds().size()) {
            String message = MessageFormat.format("Unable to reset rep orders sent for billing as only {0} rep order(s) could be processed (from a total of {1} rep order(s))", updatedRows, request.getIds().size());
            log.error(message);
            throw new MAATCourtDataException(message);
        }
    }
}
