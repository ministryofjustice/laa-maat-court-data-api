package gov.uk.courtdata.laastatus.processor;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.link.processor.OffenceInfoProcessor;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.repository.OffenceRepository;
import gov.uk.courtdata.util.DateUtil;
import org.springframework.stereotype.Component;

import static gov.uk.courtdata.constants.CourtDataConstants.G_NO;
import static gov.uk.courtdata.constants.CourtDataConstants.LEADING_ZERO_3;
import static gov.uk.courtdata.util.DateUtil.parse;

@Component
@XRayEnabled
public class UpdateOffenceInfoProcessor extends OffenceInfoProcessor {

    public UpdateOffenceInfoProcessor(OffenceRepository offenceRepository) {
        super(offenceRepository);
    }

    @Override
    protected OffenceEntity buildOffences(Offence offence, CourtDataDTO courtDataDTO) {
        return OffenceEntity.builder()
                .caseId(courtDataDTO.getCaseId())
                .txId(courtDataDTO.getTxId())
                .asnSeq(String.format(LEADING_ZERO_3, Integer.parseInt(offence.getAsnSeq())))
                .offenceCode(offence.getOffenceCode())
                .offenceClassification(offence.getOffenceClassification())
                .legalAidStatus(offence.getLegalAidStatus())
                .legalAidStatusDate(DateUtil.parse(offence.getLegalAidStatusDate()))
                .legalaidReason(offence.getLegalAidReason())
                .offenceDate(parse((offence.getOffenceDate())))
                .offenceShortTitle(offence.getOffenceShortTitle())
                .modeOfTrial(offence.getModeOfTrial())
                .offenceWording(offence.getOffenceWording())
                .iojDecision(offence.getIojDecision())
                .wqOffence(offence.getWqOffence())
                .applicationFlag(G_NO)
                .offenceId(offence.getOffenceId())
                .build();
    }


}
