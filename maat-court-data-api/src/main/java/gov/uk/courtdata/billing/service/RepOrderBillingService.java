package gov.uk.courtdata.billing.service;

import gov.uk.courtdata.billing.dto.RepOrderBillingDTO;
import gov.uk.courtdata.billing.mapper.RepOrderBillingMapper;
import gov.uk.courtdata.billing.request.UpdateRepOrderBillingRequest;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.RepOrderRepository;
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

    private final RepOrderRepository repOrderRepository;

    public List<RepOrderBillingDTO> getRepOrdersForBilling() {
        List<RepOrderEntity> extractedRepOrders = repOrderRepository.getRepOrdersForBilling();

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

        int updatedRows = repOrderRepository.resetBillingFlagForRepOrderIds(
            request.getUserModified(), request.getRepOrderIds());

        if (updatedRows != request.getRepOrderIds().size()) {
            throw new MAATCourtDataException("Unable to reset rep orders sent for billing");
        }
    }
}
