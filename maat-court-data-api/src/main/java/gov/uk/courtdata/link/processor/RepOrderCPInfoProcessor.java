package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class RepOrderCPInfoProcessor implements Process {

    private final RepOrderCPDataRepository repOrderDataRepository;

    @Override
    public void process(CourtDataDTO courtDataDTO) {

        final CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        Optional<RepOrderCPDataEntity> repOrderEntity = repOrderDataRepository.findByrepOrderId(caseDetails.getMaatId());

        RepOrderCPDataEntity repOrder = repOrderEntity.get();
        repOrder.setCaseUrn(caseDetails.getCaseUrn());
        repOrder.setDefendantId(caseDetails.getDefendant().getDefendantId());
        repOrderDataRepository.save(repOrder);

    }
}
