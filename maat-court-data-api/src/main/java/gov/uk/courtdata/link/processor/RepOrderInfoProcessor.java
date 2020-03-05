package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.CourtDataDTO;
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
    public void process(CourtDataDTO courtDataDTO) {

        Optional<RepOrderCPDataEntity> repOrderEntity = repOrderDataRepository.findByrepOrderId(courtDataDTO.getCaseDetails().getMaatId());

        RepOrderCPDataEntity repOrder = repOrderEntity.get();
        repOrder.setDefendantId(courtDataDTO.getCaseDetails().getDefendant().getDefendantId());
        repOrderDataRepository.save(repOrder);

    }
}
