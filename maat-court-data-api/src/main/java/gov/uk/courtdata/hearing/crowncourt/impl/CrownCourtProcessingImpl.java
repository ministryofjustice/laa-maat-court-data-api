package gov.uk.courtdata.hearing.crowncourt.impl;

import gov.uk.courtdata.entity.CrownCourtCode;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.hearing.crowncourt.validator.CrownCourtValidationProcessor;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.*;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;


import static gov.uk.courtdata.enums.CrownCourtCaseType.APPEAL_CC;
import static java.lang.String.format;

@Component
@RequiredArgsConstructor
@Slf4j
public class CrownCourtProcessingImpl {

    private final CrownCourtProcessingRepository crownCourtProcessingRepository;

    private final RepOrderRepository repOrderRepository;

    private final CrownCourtCodeRepository crownCourtCodeRepository;

    private final CrownCourtProcessHelper crownCourtProcessHelper;

    private final CrownCourtStoredProcedureRepository crownCourtStoredProcedureRepository;

    private final CalculateCrownCourtOutCome calculateCrownCourtOutCome;

    private final CrownCourtValidationProcessor crownCourtValidationProcessor;

    @Value("${spring.datasource.username}")
    private String dbUser;

    public void execute(HearingResulted hearingResulted) {

        String crownCourtOutCome = calculateCrownCourtOutCome.calculate(hearingResulted);
        crownCourtValidationProcessor.validate(hearingResulted, crownCourtOutCome);

        final Integer maatId = hearingResulted.getMaatId();
        final Optional<RepOrderEntity> optionalRepEntity = repOrderRepository.findById(maatId);

        if (optionalRepEntity.isPresent()) {

            final String crownCourtCode = getCCCode(hearingResulted.getSession().getCourtLocation());
            RepOrderEntity repOrderEntity = optionalRepEntity.get();

            crownCourtStoredProcedureRepository.updateCrownCourtOutcome(maatId,
                    crownCourtOutCome,
                    crownCourtProcessHelper.isBenchWarrantIssued(hearingResulted, crownCourtOutCome),
                    repOrderEntity.getAptyCode(),
                    crownCourtProcessHelper.isImprisoned(hearingResulted, crownCourtOutCome),
                    hearingResulted.getCaseUrn(),
                    crownCourtCode);

            processSentencingDate(hearingResulted.getCaseEndDate(), maatId, repOrderEntity.getCatyCaseType());
        }
    }

    private void processSentencingDate(String ccCaseEndDate, Integer maatId, String catyType) {

        LocalDate caseEndDate = DateUtil.parse(ccCaseEndDate);
        if (caseEndDate != null) {
            String user = dbUser != null ? dbUser.toUpperCase() : null;
            if (APPEAL_CC.getValue().equalsIgnoreCase(catyType)) {
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
