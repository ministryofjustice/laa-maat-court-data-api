package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.CreateLinkDto;
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

    private final WqCoreRepository wqCoreRepository;

    @Override
    public void process(CreateLinkDto saveAndLinkModel) {

        CaseDetails caseDetails = saveAndLinkModel.getCaseDetails();
        WqCoreEntity wqCoreEntity = WqCoreEntity.builder()
                .txId(saveAndLinkModel.getTxId())
                .caseId(saveAndLinkModel.getCaseId())
                .createdTime(LocalDate.now())
                .createdUserId(caseDetails.getCreatedUser())
                .wqType(WQ_CREATION_EVENT)
                .wqStatus(WQ_SUCCESS_STATUS)
                .build();
        wqCoreRepository.save(wqCoreEntity);
    }
}
