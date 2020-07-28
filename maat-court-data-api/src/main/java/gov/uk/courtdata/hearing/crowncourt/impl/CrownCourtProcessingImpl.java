package gov.uk.courtdata.hearing.crowncourt.impl;

import gov.uk.courtdata.entity.CrownCourtCode;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.model.hearing.CCOutComeData;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.CrownCourtCodeRepository;
import gov.uk.courtdata.repository.CrownCourtProcessingRepository;
import gov.uk.courtdata.repository.CrownCourtStoredProcedureRepository;
import gov.uk.courtdata.repository.RepOrderRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

import static gov.uk.courtdata.constants.CourtDataConstants.CATY_CASE_TYPE;
import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class CrownCourtProcessingImpl {

    private final CrownCourtProcessingRepository crownCourtProcessingRepository;

    private final RepOrderRepository repOrderRepository;

    private final CrownCourtCodeRepository crownCourtCodeRepository;

    private final CrownCourtProcessHelper crownCourtProcessHelper;

    private final CrownCourtStoredProcedureRepository crownCourtStoredProcedureRepository;


    @Value("${spring.datasource.username}")
    private String dbUser;


    public void execute(HearingResulted hearingResulted) {

        CCOutComeData ccutComeData = hearingResulted.getCcOutComeData();
        final Integer maatId = hearingResulted.getMaatId();
        final Optional<RepOrderEntity> optionalRepEntity = repOrderRepository.findById(maatId);


        if (optionalRepEntity.isPresent()) {


            final String crownCourtCode = getCCCode(hearingResulted.getSession().getCourtLocation());
            RepOrderEntity repOrderEntity = optionalRepEntity.get();

            crownCourtStoredProcedureRepository.updateCrownCourtOutcome(maatId,
                    ccutComeData.getCcooOutcome(),
                    crownCourtProcessHelper.isBenchWarrantIssued(hearingResulted),
                    ccutComeData.getAppealType() != null ? ccutComeData.getAppealType() : repOrderEntity.getAptyCode(),
                    crownCourtProcessHelper.isImprisoned(hearingResulted, ccutComeData.getCcooOutcome()),
                    hearingResulted.getCaseUrn(),
                    crownCourtCode);


            processSentencingDate(ccutComeData.getCaseEndDate(), maatId, repOrderEntity.getCatyCaseType());
        }
    }


    private void processSentencingDate(String ccCaseEndDate, Integer maatId, String catyType) {

        LocalDate caseEndDate = DateUtil.parse(ccCaseEndDate);

        if (caseEndDate != null) {
            String user = dbUser != null ? dbUser.toUpperCase() : null;
            if (CATY_CASE_TYPE.equalsIgnoreCase(catyType)) {
                crownCourtProcessingRepository
                        .invokeUpdateAppealSentenceOrderDate(maatId, user, caseEndDate, LocalDate.now());
            } else {
                crownCourtProcessingRepository.invokeUpdateSentenceOrderDate(maatId, user, caseEndDate);

            }

        }
    }

    private String getCCCode(String ouCode) {
        Optional<CrownCourtCode> optCrownCourtCode = crownCourtCodeRepository.findByOuCode(ouCode);
        CrownCourtCode crownCourtCode = optCrownCourtCode.orElseThrow(()
                -> new MAATCourtDataException(format("Crown Court Code Look Up Failed for %s", ouCode)));
        return crownCourtCode.getCode();
    }

}
