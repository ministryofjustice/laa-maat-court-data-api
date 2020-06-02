package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.RepOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RepOrderInfoProcessor implements Process {

    private final RepOrderRepository repOrderRepository;

    @Override
    public void process(CourtDataDTO saveAndLinkModel) {

        final CaseDetails caseDetails = saveAndLinkModel.getCaseDetails();
        final Integer maatId = caseDetails.getMaatId();
        RepOrderEntity repOrderEntity = repOrderRepository.findById(maatId).get();
        repOrderEntity.setCaseId(saveAndLinkModel.getLibraId());
        if (caseDetails.getAsn() != null) {
            repOrderEntity.setArrestSummonsNo(caseDetails.getAsn());
        }
        repOrderRepository.save(repOrderEntity);
    }
}
