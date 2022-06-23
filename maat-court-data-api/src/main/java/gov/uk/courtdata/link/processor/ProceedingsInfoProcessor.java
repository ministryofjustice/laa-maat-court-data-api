package gov.uk.courtdata.link.processor;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.ProceedingEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.ProceedingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@XRayEnabled
@RequiredArgsConstructor
public class ProceedingsInfoProcessor implements Process {

    private final ProceedingRepository proceedingRepository;

    @Override
    public void process(CourtDataDTO courtDataDTO) {

        final CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        ProceedingEntity proceedingEntity = ProceedingEntity.builder()
                .maatId(caseDetails.getMaatId())
                .proceedingId(courtDataDTO.getProceedingId())
                .createdTxid(courtDataDTO.getTxId())
                .createdUser(caseDetails.getCreatedUser())
                .build();
        proceedingRepository.save(proceedingEntity);
    }
}
