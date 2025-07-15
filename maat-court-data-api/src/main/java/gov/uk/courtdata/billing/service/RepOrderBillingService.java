package gov.uk.courtdata.billing.service;

import gov.uk.courtdata.billing.dto.RepOrderBillingDTO;
import gov.uk.courtdata.billing.entity.RepOrderBillingEntity;
import gov.uk.courtdata.billing.mapper.RepOrderBillingMapper;
import gov.uk.courtdata.billing.repository.RepOrderBillingRepository;
import gov.uk.courtdata.billing.request.UpdateRepOrderBillingRequest;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.exception.ValidationException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    public void resetRepOrdersSentForBilling(UpdateRepOrderBillingRequest request) {
        if (request.getRepOrderIds().isEmpty()) {
            return;
        }

        if (StringUtils.isBlank(request.getUserModified())) {
            throw new ValidationException("Username must be provided");
        }

        int updatedRows = repOrderBillingRepository.resetBillingFlagForRepOrderIds(
            request.getUserModified(), request.getRepOrderIds());

        if (updatedRows != request.getRepOrderIds().size()) {
            String message = MessageFormat.format("Unable to reset rep orders sent for billing as only {0} rep order(s) could be processed (from a total of {1} rep order(s))", updatedRows, request.getRepOrderIds().size());
            log.error(message);
            throw new MAATCourtDataException(message);
        }
    }
}
