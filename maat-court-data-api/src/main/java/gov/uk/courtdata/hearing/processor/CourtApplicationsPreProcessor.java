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
     * Use the ref data to get the Application flag by offence code
     * ASN SEQ of existing applications = stored value, Increment it for new applications
     */
    public void process(final HearingResulted hearingResulted) {

        List<Offence> applicationList = mapApplicationFlag(hearingResulted);
        mapASNSeq(hearingResulted, applicationList);
        hearingResulted.getDefendant().setOffences(applicationList);


    }

    private void mapASNSeq(HearingResulted hearingResulted, List<Offence> applicationList) {
        List<WqLinkRegisterEntity> wqLinkRegisterEntity = wqLinkRegisterRepository
                .findBymaatId(hearingResulted.getMaatId());


        if (!wqLinkRegisterEntity.isEmpty()) {
            WqLinkRegisterEntity wqLinkReg = wqLinkRegisterEntity.get(0);
            List<Offence> existingApplications = applicationList.stream()
                    .map(offence -> setASNSeq(offence, wqLinkReg))
                    .filter(offence -> offence.getAsnSeq() != null)
                    .collect(Collectors.toList());
            List<Offence> newApplications = applicationList.stream()
                    .filter(offence -> offence.getAsnSeq() == null)
                    .map(offence -> setNewASNSeq(existingApplications, offence))
                    .collect(Collectors.toList());
            applicationList.clear();
            applicationList.addAll(existingApplications);
            applicationList.addAll(newApplications);

        }


    }

    private Offence setNewASNSeq(List<Offence> existingApplications, Offence offence) {
        offence.setAsnSeq(String.valueOf(existingApplications.size() + 1));
        return offence;
    }


    private Offence setASNSeq(Offence offence, WqLinkRegisterEntity wqLinkRegisterEntity) {
        Optional<OffenceEntity> offenceEntity = offenceRepository.findApplicationByOffenceCode(wqLinkRegisterEntity.getCaseId(),
                offence.getOffenceId(), offence.getApplicationFlag());
        offenceEntity.ifPresent(entity -> offence.setAsnSeq(entity.getAsnSeq()));
        return offence;
    }


    private List<Offence> mapApplicationFlag(HearingResulted hearingResulted) {

        return hearingResulted.getDefendant().getOffences()
                .stream()
                .filter(offence -> offence.getOffenceCode() != null)
                .map(this::setApplicationFlag).collect(Collectors.toList());

    }

    private Offence setApplicationFlag(Offence offence) {
        Optional<XLATOffenceEntity> xlatOffenceEntity = xlatOffenceRepository.findById(offence.getOffenceCode());
        xlatOffenceEntity.ifPresent(offenceEntity -> offence.setApplicationFlag(offenceEntity.getApplicationFlag()));

        return offence;
    }
}
