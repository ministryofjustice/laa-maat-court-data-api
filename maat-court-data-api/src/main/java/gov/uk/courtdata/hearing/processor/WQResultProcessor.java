package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.constants.CourtDataConstants;
import gov.uk.courtdata.entity.WQResultEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WQResultRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WQResultProcessor {

    private final WQResultRepository wqResultRepository;

    /**
     * @param magsCourtDTO
     */
    public void process(final HearingDTO magsCourtDTO) {


        WQResultEntity wqResultEntity = WQResultEntity.builder()
                .caseId(magsCourtDTO.getCaseId())
                .txId(magsCourtDTO.getTxId())
                .asn(magsCourtDTO.getAsn())
                .asnSeq(magsCourtDTO.getOffence().getAsnSeq())
                .resultCode(magsCourtDTO.getResult().getResultCode())
                .resultShortTitle(magsCourtDTO.getResult().getResultShortTitle())
                .resultText(StringUtils.truncate(
                        magsCourtDTO.getResult().getResultText(), CourtDataConstants.ORACLE_VARCHAR_MAX))
                .resultCodeQualifiers(magsCourtDTO.getResult().getResultCodeQualifiers())
                .nextHearingDate(DateUtil.parse(magsCourtDTO.getResult().getNextHearingDate()))
                .nextHearingLocation(magsCourtDTO.getResult().getNextHearingLocation())
                .firmName(magsCourtDTO.getResult().getFirmName())
                .contactName(magsCourtDTO.getResult().getContactName())
                .laaOfficeAccount(magsCourtDTO.getResult().getLaaOfficeAccount())
                .legalAidWithdrawalDate(DateUtil.parse(magsCourtDTO.getResult().getLegalAidWithdrawalDate()))
                .dateOfHearing(DateUtil.parse(magsCourtDTO.getSession().getDateOfHearing()))
                .courtLocation(magsCourtDTO.getSession().getCourtLocation())
                .sessionValidateDate(DateUtil.parse(magsCourtDTO.getSession().getSessionValidatedDate()))
                .jurisdictionType(magsCourtDTO.getJurisdictionType().name())
                .build();

        wqResultRepository.save(wqResultEntity);
    }


}
