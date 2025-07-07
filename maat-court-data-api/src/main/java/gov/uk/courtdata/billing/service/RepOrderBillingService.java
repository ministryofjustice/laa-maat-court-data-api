package gov.uk.courtdata.billing.service;

import gov.uk.courtdata.billing.dto.RepOrderBillingDTO;
import gov.uk.courtdata.billing.mapper.RepOrderBillingMapper;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.repository.RepOrderRepository;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
