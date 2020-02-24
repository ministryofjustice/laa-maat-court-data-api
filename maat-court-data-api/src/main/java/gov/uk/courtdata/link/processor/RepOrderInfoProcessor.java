package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.CreateLinkDto;
import gov.uk.courtdata.entity.RepOrderCommonPlatformDataEntity;
import gov.uk.courtdata.repository.RepOrderCommonPlatformDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class RepOrderInfoProcessor implements Process {

    private final RepOrderCommonPlatformDataRepository repOrderDataRepository;

    @Override
    public void process(CreateLinkDto saveAndLinkModel) {

        Optional<RepOrderCommonPlatformDataEntity> repOrderEntity = repOrderDataRepository.findByrepOrderId(saveAndLinkModel.getCaseDetails().getMaatId());
        if (repOrderEntity.isPresent()) {
            RepOrderCommonPlatformDataEntity repOrder = repOrderEntity.get();
            repOrder.setDefendantId(saveAndLinkModel.getCaseDetails().getDefendant().getDefendantId());
            repOrderDataRepository.save(repOrder);
        }
    }
}
