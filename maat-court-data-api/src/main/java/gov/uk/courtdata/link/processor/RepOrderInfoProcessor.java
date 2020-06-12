package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.RepOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class RepOrderInfoProcessor implements Process {

    private final RepOrderRepository repOrderRepository;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Override
    public void process(CourtDataDTO saveAndLinkModel) {

        final CaseDetails caseDetails = saveAndLinkModel.getCaseDetails();
        final Integer maatId = caseDetails.getMaatId();
        final Optional<RepOrderEntity> optRepOrder = repOrderRepository.findById(maatId);
        if (optRepOrder.isPresent()) {
            RepOrderEntity repOrderEntity = optRepOrder.get();

            repOrderEntity.setCaseId(saveAndLinkModel.getLibraId());
            if (caseDetails.getAsn() != null) {
                repOrderEntity.setArrestSummonsNo(caseDetails.getAsn());
            }
            repOrderEntity.setDateModified(LocalDateTime.now());
            repOrderEntity.setUserModified(dbUser);
            repOrderRepository.save(repOrderEntity);
        }
    }
}
