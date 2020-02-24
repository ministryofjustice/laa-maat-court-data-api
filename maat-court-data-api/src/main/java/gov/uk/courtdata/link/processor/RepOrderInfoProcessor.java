package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.CreateLinkDto;
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
    public void process(CreateLinkDto saveAndLinkModel) {

        Optional<RepOrderCPDataEntity> repOrderEntity = repOrderDataRepository.findByrepOrderId(saveAndLinkModel.getCaseDetails().getMaatId());
        if (repOrderEntity.isPresent()) {
            RepOrderCPDataEntity repOrder = repOrderEntity.get();
            repOrder.setDefendantId(saveAndLinkModel.getCaseDetails().getDefendant().getDefendantId());
            repOrderDataRepository.save(repOrder);
        }
    }
}
