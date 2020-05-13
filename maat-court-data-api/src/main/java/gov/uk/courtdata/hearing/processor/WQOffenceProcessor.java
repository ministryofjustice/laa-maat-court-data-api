package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.WQOffence;
import gov.uk.courtdata.hearing.magistrate.dto.MagistrateCourtDTO;
import gov.uk.courtdata.repository.WQOffenceRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static gov.uk.courtdata.constants.CourtDataConstants.G_NO;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Component
@RequiredArgsConstructor
public class WQOffenceProcessor {

    private final WQOffenceRepository wqOffenceRepository;


    public void process(final MagistrateCourtDTO magsCourtDTO) {


        WQOffence wqOffence = WQOffence.builder()
                .caseId(magsCourtDTO.getCaseId())
                .txId(magsCourtDTO.getTxId())
                .asnSeq(magsCourtDTO.getOffence().getAsnSeq())
                .offenceCode(magsCourtDTO.getOffence().getOffenceCode())
                .offenceClassification(magsCourtDTO.getOffence().getOffenceClassification())
                .legalAidStatus(mapLegalAidStatus(magsCourtDTO.getOffence().getLegalAidStatus()))
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


    /**
     * Map legacy codes as in stored proc.
     *
     * @param legalAidStatus
     * @return
     */
    private String mapLegalAidStatus(String legalAidStatus) {

        if (isEmpty(legalAidStatus))
            return "AP";

        switch (legalAidStatus) {

            case "RE":
                return "FB";
            case "VA":
                return "GR";
            case "WI":
                return "WD";
            default:
                return legalAidStatus;
        }

    }


}
