package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.entity.XLATOffenceEntity;
import gov.uk.courtdata.enums.ApplicationClassification;
import gov.uk.courtdata.enums.ModeOfTrial;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.OffenceRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import gov.uk.courtdata.repository.XLATOffenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static gov.uk.courtdata.constants.CourtDataConstants.APPLICATION_ASN_SEQ_INITIAL_VALUE;
import static gov.uk.courtdata.constants.CourtDataConstants.NO;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourtApplicationsPreProcessor {

    private final XLATOffenceRepository xlatOffenceRepository;
    private final OffenceRepository offenceRepository;
    private final WqLinkRegisterRepository wqLinkRegisterRepository;


    public void process(final HearingResulted hearingResulted) {

        mapApplicationFlag(hearingResulted);
        mapASNSeq(hearingResulted);
        mapApplicationDefaults(hearingResulted);
    }

    private void mapASNSeq(HearingResulted hearingResulted) {

        List<WqLinkRegisterEntity> wqLinkRegisterEntity = wqLinkRegisterRepository
                .findBymaatId(hearingResulted.getMaatId());

        if (!wqLinkRegisterEntity.isEmpty()) {

            WqLinkRegisterEntity wqLinkReg = wqLinkRegisterEntity.get(0);

            List<Offence> offenceList = hearingResulted.getDefendant().getOffences();

            processASNSeqForExistingApp(wqLinkReg, offenceList);

            processASNSeqForNewApp(offenceList);

        }
    }

    private void processASNSeqForNewApp(List<Offence> offenceList) {

        List<Offence> newApplications = offenceList.stream()
                .filter(offence -> offence.getAsnSeq() == null)
                .collect(Collectors.toList());

        if (!newApplications.isEmpty()) {
            int count = APPLICATION_ASN_SEQ_INITIAL_VALUE + (int) (offenceList
                    .stream()
                    .filter(offence -> offence.getAsnSeq() != null)
                    .count());

            for (Offence offence : newApplications) {
                offence.setAsnSeq(String.valueOf(count));
                count++;
            }
        }
    }

    private void processASNSeqForExistingApp(WqLinkRegisterEntity wqLinkReg, List<Offence> offenceList) {
        offenceList.forEach(offence -> setASNSeqOnExistingApp(offence, wqLinkReg));

    }

    private void setASNSeqOnExistingApp(Offence offence, WqLinkRegisterEntity wqLinkRegisterEntity) {
        Optional<OffenceEntity> offenceEntity =
                offenceRepository.findApplicationByOffenceCode(wqLinkRegisterEntity.getCaseId(),
                                                               offence.getOffenceId(), offence.getApplicationFlag()
                );
        offenceEntity.ifPresent(entity -> offence.setAsnSeq(entity.getAsnSeq()));
    }


    private void mapApplicationFlag(HearingResulted hearingResulted) {

        hearingResulted.getDefendant().getOffences().forEach(this::setApplicationFlag);

    }

    private void setApplicationFlag(Offence offence) {
        Optional<XLATOffenceEntity> xlatOffenceEntity = xlatOffenceRepository.findById(offence.getOffenceCode());
        xlatOffenceEntity.ifPresent(offenceEntity -> offence.setApplicationFlag(offenceEntity.getApplicationFlag()));

    }

    private void mapApplicationDefaults(HearingResulted hearingResulted) {
        hearingResulted.getDefendant().getOffences().forEach(offence -> offence.setModeOfTrial(ModeOfTrial.NO_MODE_OF_TRAIL.value()));
        hearingResulted.setInActive(NO);
        hearingResulted.getDefendant().getOffences()
                .forEach(offence -> offence.setOffenceClassification(ApplicationClassification
                                                                             .getDescriptionByCode(
                                                                                     offence.getOffenceClassification())));
    }
}
