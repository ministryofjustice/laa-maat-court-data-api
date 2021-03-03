package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.entity.XLATOffenceEntity;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class CourtApplicationsPreProcessor {

    private final XLATOffenceRepository xlatOffenceRepository;
    private final OffenceRepository offenceRepository;
    private final WqLinkRegisterRepository wqLinkRegisterRepository;

    /**
     * This process set Application Flags & ANS SEQ for court application data feed
     * to fit into the current legacy MLRA set up
     */
    public void process(final HearingResulted hearingResulted) {
        List<Offence> applicationList;
        applicationList = mapApplicationFlag(hearingResulted);
        mapASNSeq(applicationList);
        hearingResulted.getDefendant().setOffences(applicationList);


    }

    private void mapASNSeq(HearingResulted hearingResulted) {
        WqLinkRegisterEntity wqLinkRegisterEntity = wqLinkRegisterRepository
                .findBymaatId(hearingResulted.getMaatId())
                .stream()
                .findFirst()
                .orElse(null);


        if (wqLinkRegisterEntity != null) {
            List<Offence> existingApplications = hearingResulted.getDefendant().getOffences().stream()
                    .map(offence -> setASNSeq(offence, wqLinkRegisterEntity))
                    .filter(offence -> offence.getAsnSeq() != null)
                    .collect(Collectors.toList());
            List<Offence> newApplications = hearingResulted.getDefendant().getOffences().stream()
                    .filter(offence -> offence.getAsnSeq() == null)
                    .map(offence -> setNewASNSeq(existingApplications, offence))
                    .collect(Collectors.toList());


        }


    }

    private Offence setNewASNSeq(List<Offence> existingApplications, Offence offence) {
        offence.setAsnSeq(String.valueOf(existingApplications.size() + 1));
        return offence;
    }


    private Offence setASNSeq(Offence offence, WqLinkRegisterEntity wqLinkRegisterEntity) {
        Optional<OffenceEntity> offenceEntity = offenceRepository.findApplicationByOffenceCode(wqLinkRegisterEntity.getCaseId(),
                offence.getOffenceId(), offence.getOffenceClassification());
        offenceEntity.ifPresent(entity -> offence.setAsnSeq(entity.getAsnSeq()));
        return offence;
    }


    private List<Offence> mapApplicationFlag(HearingResulted hearingResulted) {

        return hearingResulted.getDefendant().getOffences()
                .stream()
                .filter(offence -> offence.getOffenceId() != null)
                .map(this::setApplicationFlag).collect(Collectors.toList());

    }

    private Offence setApplicationFlag(Offence offence) {
        Optional<XLATOffenceEntity> xlatOffenceEntity = xlatOffenceRepository.findById(offence.getOffenceCode());
        xlatOffenceEntity.ifPresent(offenceEntity -> offence.setOffenceClassification(String.valueOf(offenceEntity.getApplicationFlag())));

        return offence;
    }
}
