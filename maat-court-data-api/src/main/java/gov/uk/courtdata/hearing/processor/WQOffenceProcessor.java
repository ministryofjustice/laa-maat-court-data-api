package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.WQOffence;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.hearing.dto.OffenceDTO;
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

    public void process(final HearingDTO magsCourtDTO) {


        final OffenceDTO offence = magsCourtDTO.getOffence();

        WQOffence wqOffence = WQOffence.builder()
                .caseId(magsCourtDTO.getCaseId())
                .txId(magsCourtDTO.getTxId())
                .asnSeq(offence.getAsnSeq())
                .offenceCode(offence.getOffenceCode())
                .offenceClassification(offence.getOffenceClassification())
                .legalAidStatus(mapLegalAidStatus(offence.getLegalAidStatus()))
                .legalAidStatusDate(DateUtil.parse(offence.getLegalAidStatusDate()))
                .legalaidReason(offence.getLegalAidReason())
                .offenceDate(DateUtil.parse(offence.getOffenceDate()))
                .offenceShortTitle(offence.getOffenceShortTitle())
                .modeOfTrial(offence.getModeOfTrial())
                .offenceWording(offence.getOffenceWording())
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
