package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.WQResult;
import gov.uk.courtdata.hearing.magistrate.dto.MagistrateCourtDTO;
import gov.uk.courtdata.repository.WQResultRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WQResultProcessor {

    private final WQResultRepository wqResultRepository;

    /**
     * @param magsCourtDTO
     */
    public void process(final MagistrateCourtDTO magsCourtDTO) {


        WQResult wqResult = WQResult.builder()
                .caseId(magsCourtDTO.getCaseId())
                .txId(magsCourtDTO.getTxId())
                .asn(magsCourtDTO.getAsn())
                .asnSeq(magsCourtDTO.getOffence().getAsnSeq())
                .resultCode(magsCourtDTO.getResult().getResultCode())
                .resultShortTitle(magsCourtDTO.getResult().getResultShortTitle())
                .resultText(magsCourtDTO.getResult().getResultText())
                .resultCodeQualifiers(magsCourtDTO.getResult().getResultCodeQualifiers())
                .nextHearingDate(DateUtil.toDate(magsCourtDTO.getResult().getNextHearingDate()))
                .nextHearingLocation(magsCourtDTO.getResult().getNextHearingLocation())
                .firmName(magsCourtDTO.getResult().getFirmName())
                .contactName(magsCourtDTO.getResult().getContactName())
                .laaOfficeAccount(magsCourtDTO.getResult().getLaaOfficeAccount())
                .legalAidWithdrawalDate(DateUtil.toDate(magsCourtDTO.getResult().getLegalAidWithdrawalDate()))
                .dateOfHearing(DateUtil.toDate(magsCourtDTO.getSession().getDateOfHearing()))
                .courtLocation(magsCourtDTO.getSession().getCourtLocation())
                .sessionValidateDate(DateUtil.toDate(magsCourtDTO.getSession().getSessionValidatedDate()))
                .build();

        wqResultRepository.save(wqResult);
    }


}
