package gov.uk.courtdata.hearing.processor;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.constants.CourtDataConstants;
import gov.uk.courtdata.entity.WQOffenceEntity;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.hearing.dto.HearingOffenceDTO;
import gov.uk.courtdata.prosecutionconcluded.helper.OffenceHelper;
import gov.uk.courtdata.repository.WQOffenceRepository;
import gov.uk.courtdata.util.DateUtil;
import gov.uk.courtdata.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static gov.uk.courtdata.constants.CourtDataConstants.*;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Component
@XRayEnabled
@RequiredArgsConstructor
public class WQOffenceProcessor {

    private final WQOffenceRepository wqOffenceRepository;
    private final OffenceHelper offenceHelper;

    public void process(final HearingDTO magsCourtDTO) {


        final HearingOffenceDTO offence = magsCourtDTO.getOffence();

        WQOffenceEntity wqOffenceEntity = WQOffenceEntity.builder()
                .caseId(magsCourtDTO.getCaseId())
                .txId(magsCourtDTO.getTxId())
                .asnSeq(String.format(LEADING_ZERO_3, Integer.parseInt(offence.getAsnSeq())))
                .offenceCode(offence.getOffenceCode())
                .offenceClassification(offence.getOffenceClassification())
                .legalAidStatus(mapLegalAidStatus(offence.getLegalAidStatus()))
                .legalAidStatusDate(DateUtil.parse(offence.getLegalAidStatusDate()))
                .legalaidReason(offence.getLegalAidReason())
                .offenceDate(DateUtil.parse(offence.getOffenceDate()))
                .offenceShortTitle(offence.getOffenceShortTitle())
                .modeOfTrial(offence.getModeOfTrial())
                .offenceWording(StringUtils.applyMaxLengthLimitToString(
                        offence.getOffenceWording(), CourtDataConstants.ORACLE_VARCHAR_MAX))
                .wqOffence(null)
                .applicationFlag(offence.getApplicationFlag() != null ? offence.getApplicationFlag() : G_NO)
                .offenceId(offence.getOffenceId())
                .isCCNewOffence(isCCNewOffence(magsCourtDTO))
                .build();

        wqOffenceRepository.save(wqOffenceEntity);
    }

    private String isCCNewOffence(HearingDTO magsCourtDTO) {
        String isCCNewOffence = NO;
        if (JurisdictionType.CROWN == magsCourtDTO.getJurisdictionType()
                && offenceHelper.isNewOffence(magsCourtDTO.getCaseId(), magsCourtDTO.getOffence().getAsnSeq())) {
            isCCNewOffence = YES;
        }

        return isCCNewOffence;
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
