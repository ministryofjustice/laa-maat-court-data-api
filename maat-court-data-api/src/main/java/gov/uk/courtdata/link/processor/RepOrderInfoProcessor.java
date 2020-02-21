package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.model.SaveAndLinkModel;
import gov.uk.courtdata.repository.RepOrderDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class RepOrderInfoProcessor implements Process {

    private final RepOrderDataRepository repOrderDataRepository;
    @Override
    public void process(SaveAndLinkModel saveAndLinkModel) {

        Optional<RepOrderEntity> repOrderEntity = repOrderDataRepository.findByrepOrderId(saveAndLinkModel.getCaseDetails().getMaatId());
        if (repOrderEntity.isPresent()) {
            RepOrderEntity repOrder = repOrderEntity.get();
            repOrder.setDefendantId(saveAndLinkModel.getCaseDetails().getDefendant().getDefendantId());
            repOrderDataRepository.save(repOrder);
        }
    }
}
