package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.LaaModelManager;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class RepOrderInfoProcessor implements Process {

    private final RepOrderCPDataRepository repOrderDataRepository;

    @Override
    public void process(LaaModelManager laaModelManager) {

        Optional<RepOrderCPDataEntity> repOrderEntity = repOrderDataRepository.findByrepOrderId(laaModelManager.getCaseDetails().getMaatId());
        if (repOrderEntity.isPresent()) {
            RepOrderCPDataEntity repOrder = repOrderEntity.get();
            repOrder.setDefendantId(laaModelManager.getCaseDetails().getDefendant().getDefendantId());
            repOrderDataRepository.save(repOrder);
        }
    }
}
