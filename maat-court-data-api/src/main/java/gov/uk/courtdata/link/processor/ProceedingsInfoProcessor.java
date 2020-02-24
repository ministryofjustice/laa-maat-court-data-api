package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.CreateLinkDto;
import gov.uk.courtdata.entity.ProceedingEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.ProceedingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProceedingsInfoProcessor implements Process {

    private final ProceedingRepository proceedingRepository;

    @Override
    public void process(CreateLinkDto saveAndLinkModel) {

        final CaseDetails caseDetails = saveAndLinkModel.getCaseDetails();
        ProceedingEntity proceedingEntity = ProceedingEntity.builder()
                .maatId(caseDetails.getMaatId())
                .proceedingId(saveAndLinkModel.getProceedingId())
                .createdTxid(saveAndLinkModel.getTxId())
                .createdUser(caseDetails.getCreatedUser())
                .build();
        proceedingRepository.save(proceedingEntity);
    }
}
