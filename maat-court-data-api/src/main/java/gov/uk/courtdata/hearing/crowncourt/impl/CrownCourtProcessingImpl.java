package gov.uk.courtdata.hearing.crowncourt.impl;

import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.model.hearing.CCOutComeData;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.CrownCourtProcessingRepository;
import gov.uk.courtdata.repository.RepOrderRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CrownCourtProcessingImpl {

    private final CrownCourtProcessingRepository crownCourtProcessingRepository;

    private final RepOrderRepository repOrderRepository;

    @Value("${spring.datasource.username}")
    private String dbUser;


    public void execute(HearingResulted hearingResulted) {

        CCOutComeData ccutComeData = hearingResulted.getCcOutComeData();
        final Integer maatId = hearingResulted.getMaatId();
        final Optional<RepOrderEntity> optionalRepEntity = repOrderRepository.findById(maatId);

        if (optionalRepEntity.isPresent()) {
            RepOrderEntity repOrderEntity = optionalRepEntity.get();
//            crownCourtProcessingRepository.invokeCrownCourtOutcomeProcess(maatId,
//                    ccutComeData.getCcooOutcome(),
//                    ccutComeData.getBenchWarrantIssuedYn(),
//                    ccutComeData.getAppealType() != null ? ccutComeData.getAppealType() : repOrderEntity.getAptyCode(),
//                    ccutComeData.getCcImprisioned(),na
//                    hearingResulted.getCaseUrn(),
//                    ccutComeData.getCrownCourtCode());


            processSentencingDate(ccutComeData.getCaseEndDate(), maatId, repOrderEntity.getCatyCaseType());
        }
    }

    private void processSentencingDate(String ccCaseEndDate, Integer maatId, String catyType) {

        LocalDate caseEndDate = DateUtil.parse(ccCaseEndDate);

//        if (caseEndDate != null) {
//            String user = dbUser != null ? dbUser.toUpperCase() : null;
//            if (CATY_CASE_TYPE.equalsIgnoreCase(catyType)) {
//                crownCourtProcessingRepository
//                        .invokeUpdateAppealSentenceOrderDate(maatId, user, caseEndDate, LocalDate.now());
//            } else {
//                crownCourtProcessingRepository.invokeUpdateSentenceOrderDate(maatId, user, caseEndDate);
//
//            }
//
//        }
    }
}
