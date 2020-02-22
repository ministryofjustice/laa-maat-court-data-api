package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.SaveAndLinkModel;
import gov.uk.courtdata.repository.OffenceRepository;
import gov.uk.courtdata.util.CourtDataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static gov.uk.courtdata.constants.CourtDataConstants.G_NO;
import static gov.uk.courtdata.constants.CourtDataConstants.PENDING_IOJ_DECISION;

@RequiredArgsConstructor
@Component
public class OffenceInfoProcessor implements Process {

    private final CourtDataUtil courtDataUtil;
    private final OffenceRepository offenceRepository;

    @Override
    public void process(SaveAndLinkModel saveAndLinkModel) {

        List<OffenceEntity> offenceEntityList = saveAndLinkModel.getCaseDetails().getDefendant().getOffences()
                .stream()
                .map(offence -> buildOffences(offence, saveAndLinkModel))
                .collect(Collectors.toList());
        offenceRepository.saveAll(offenceEntityList);
    }

    private OffenceEntity buildOffences(Offence offence, SaveAndLinkModel saveAndLinkModel) {
        return OffenceEntity.builder()
                .caseId(saveAndLinkModel.getCaseId())
                .txId(saveAndLinkModel.getTxId())
                .asnSeq(offence.getAsnSeq())
                .offenceCode(offence.getOffenceCode())
                .offenceClassification(offence.getOffenceClassification())
                .legalAidStatus(offence.getLegalAidStatus())
                .legalAidStatusDate(offence.getLegalAidStatusDate())
                .legalaidReason(offence.getLegalAidReason())
                .offenceDate(courtDataUtil.getDate(offence.getOffenceDate()))
                .offenceShortTitle(offence.getOffenceShortTitle())
                .modeOfTrial(offence.getModeOfTrail())
                .offenceWording(offence.getOffenceWording())
                .iojDecision(PENDING_IOJ_DECISION)
                .wqOffence(G_NO)
                .applicationFlag(G_NO)
                .build();
    }
}
