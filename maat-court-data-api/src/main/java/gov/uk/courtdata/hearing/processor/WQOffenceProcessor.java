package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.WQOffence;
import gov.uk.courtdata.hearing.magistrate.dto.MagistrateCourtDTO;
import gov.uk.courtdata.repository.WQOffenceRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static gov.uk.courtdata.constants.CourtDataConstants.G_NO;

@Component
@RequiredArgsConstructor
public class WQOffenceProcessor {

    private final WQOffenceRepository wqOffenceRepository;


    public void process(final MagistrateCourtDTO magsCourtDTO) {


        //TODO: xlat offence related

        WQOffence wqOffence = WQOffence.builder()
                .caseId(magsCourtDTO.getCaseId())
                .txId(magsCourtDTO.getTxId())
                .asnSeq(magsCourtDTO.getOffence().getAsnSeq())
                .offenceCode(magsCourtDTO.getOffence().getOffenceCode())
                .offenceClassification(magsCourtDTO.getOffence().getOffenceClassification())
                .legalAidStatus(magsCourtDTO.getOffence().getLegalAidStatus())
                .legalAidStatusDate(DateUtil.toDate(magsCourtDTO.getOffence().getLegalAidStatusDate()))
                .legalaidReason(magsCourtDTO.getOffence().getLegalAidReason())
                .offenceDate(DateUtil.toDate(magsCourtDTO.getOffence().getOffenceDate()))
                .offenceShortTitle(magsCourtDTO.getOffence().getOffenceShortTitle())
                .modeOfTrial(magsCourtDTO.getOffence().getModeOfTrial())
                .offenceWording(magsCourtDTO.getOffence().getOffenceWording())
                .wqOffence(null)
                .applicationFlag(G_NO)
                .build();

        wqOffenceRepository.save(wqOffence);
    }

}
