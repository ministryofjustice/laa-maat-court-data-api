package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.LaaModelManager;
import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.model.Offence;
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
    public void process(LaaModelManager laaModelManager) {

        List<OffenceEntity> offenceEntityList = laaModelManager.getCaseDetails().getDefendant().getOffences()
                .stream()
                .map(offence -> buildOffences(offence, laaModelManager))
                .collect(Collectors.toList());
        offenceRepository.saveAll(offenceEntityList);
    }

    protected OffenceEntity buildOffences(Offence offence, LaaModelManager laaModelManager) {
        return OffenceEntity.builder()
                .caseId(laaModelManager.getCaseId())
                .txId(laaModelManager.getTxId())
                .asnSeq(offence.getAsnSeq())
                .offenceCode(offence.getOffenceCode())
                .offenceClassification(offence.getOffenceClassification())
                .legalAidStatus(offence.getLegalAidStatus())
                .legalAidStatusDate(courtDataUtil.getDate(offence.getLegalAidStatusDate()))
                .legalaidReason(offence.getLegalAidReason())
                .offenceDate(courtDataUtil.getDate(offence.getOffenceDate()))
                .offenceShortTitle(offence.getOffenceShortTitle())
                .modeOfTrial(offence.getModeOfTrail())
                .offenceWording(offence.getOffenceWording())
                .iojDecision(getIojDecision(offence))
                .wqOffence(getWQOffence(offence))
                .applicationFlag(G_NO)
                .build();
    }

    protected Integer getWQOffence(Offence offence) {
        return G_NO;
    }

    protected Integer getIojDecision(Offence offence) {
        return PENDING_IOJ_DECISION;
    }
}
