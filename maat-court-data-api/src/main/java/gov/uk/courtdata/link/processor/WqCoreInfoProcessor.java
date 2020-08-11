package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.enums.WQStatus;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.WqCoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static gov.uk.courtdata.constants.CourtDataConstants.WQ_CREATION_EVENT;


@RequiredArgsConstructor
@Component
public class WqCoreInfoProcessor implements Process {

    protected final WqCoreRepository wqCoreRepository;

    @Value("${feature.postMvpEnabled}")
    private String isPostMVPEnabled;


    @Override
    public void process(CourtDataDTO courtDataDTO) {

        CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        WqCoreEntity wqCoreEntity = WqCoreEntity.builder()
                .txId(courtDataDTO.getTxId())
                .caseId(courtDataDTO.getCaseId())
                .createdTime(LocalDate.now())
                .createdUserId(caseDetails.getCreatedUser())
                .wqType(getWQEvent())
                .wqStatus(Boolean.TRUE.toString().equalsIgnoreCase(isPostMVPEnabled) ? WQStatus.WAITING.value() : WQStatus.SUCCESS.value())
                .build();
        wqCoreRepository.save(wqCoreEntity);
    }

    protected Integer getWQEvent() {
        return WQ_CREATION_EVENT;
    }
}
