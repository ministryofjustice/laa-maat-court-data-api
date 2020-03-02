package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.LaaModelManager;
import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.WqCoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static gov.uk.courtdata.constants.CourtDataConstants.WQ_CREATION_EVENT;
import static gov.uk.courtdata.constants.CourtDataConstants.WQ_SUCCESS_STATUS;


@RequiredArgsConstructor
@Component
public class WqCoreInfoProcessor implements Process {

    protected final WqCoreRepository wqCoreRepository;

    @Override
    public void process(LaaModelManager laaModelManager) {

        CaseDetails caseDetails = laaModelManager.getCaseDetails();
        WqCoreEntity wqCoreEntity = WqCoreEntity.builder()
                .txId(laaModelManager.getTxId())
                .caseId(laaModelManager.getCaseId())
                .createdTime(LocalDate.now())
                .createdUserId(caseDetails.getCreatedUser())
                .wqType(getWQEvent())
                .wqStatus(WQ_SUCCESS_STATUS)
                .build();
        wqCoreRepository.save(wqCoreEntity);
    }

    protected Integer getWQEvent() {
        return WQ_CREATION_EVENT;
    }
}
