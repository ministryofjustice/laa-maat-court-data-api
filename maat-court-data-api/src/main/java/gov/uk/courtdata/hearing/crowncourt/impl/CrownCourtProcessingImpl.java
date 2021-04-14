package gov.uk.courtdata.hearing.crowncourt.impl;

import gov.uk.courtdata.entity.CrownCourtCode;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.enums.CrownCourtTrialOutcome;
import gov.uk.courtdata.enums.PleaTrialOutcome;
import gov.uk.courtdata.enums.VerdictTrialOutcome;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.hearing.crowncourt.validator.CrownCourtValidationProcessor;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.hearing.CCOutComeData;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.*;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private final OffenceHelper offenceHelper;
    private final CrownCourtValidationProcessor crownCourtValidationProcessor;

    @Value("${spring.datasource.username}")
    private String dbUser;

    public void execute(HearingResulted hearingResulted) {

        String crownCourtOutCome = calculateCrownCourtOutCome(hearingResulted);
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

    private String calculateCrownCourtOutCome(HearingResulted hearingResulted) {

        if (hearingResulted.isProsecutionConcluded()) {
            List<String> offenceOutcomeList = new ArrayList<>();
            List<Offence> offenceList = offenceHelper.getOffences(hearingResulted.getMaatId());

            offenceList
                    .forEach(offence -> {

                        if (offence.getVerdict() != null) {
                            offenceOutcomeList.add(VerdictTrialOutcome.getTrialOutcome(offence.getVerdict().getCategoryType()));
                        } else if (offence.getPlea() != null) {
                            offenceOutcomeList.add(PleaTrialOutcome.getTrialOutcome(offence.getPlea().getPleaValue()));
                        }
                    });

            List<String> outcomes = offenceOutcomeList.stream().distinct().collect(Collectors.toList());
            log.info("Offence count: " + outcomes.stream().collect(Collectors.joining(", ")));
            String offenceOutcomeStatus="";

            if (outcomes.size() == 1) {
                offenceOutcomeStatus = outcomes.get(0);
            } else if (outcomes.size() > 1) {
                offenceOutcomeStatus = CrownCourtTrialOutcome.PART_CONVICTED.getValue();
            }
            log.info("Calculated crown court outcome. " + offenceOutcomeStatus);
            return offenceOutcomeStatus;
        }
        return null;
    }
}
