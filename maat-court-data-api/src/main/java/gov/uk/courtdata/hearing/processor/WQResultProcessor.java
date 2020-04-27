package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.WQResult;
import gov.uk.courtdata.entity.XLATResult;
import gov.uk.courtdata.hearing.magistrate.dto.MagistrateCourtDTO;
import gov.uk.courtdata.repository.WQResultRepository;
import gov.uk.courtdata.repository.XLATResultRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WQResultProcessor {

    private final WQResultRepository wqResultRepository;

    private final XLATResultRepository xlatResultRepository;

    public void process(final MagistrateCourtDTO magsCourtDTO) {

        //TODO: XLAT result related

        Optional<XLATResult> xlatResult =
                xlatResultRepository.findById(Integer.parseInt(magsCourtDTO.getResult().getResultCode()));

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
                .firmName(magsCourtDTO.getResult().getFirstName())
                .contactName(magsCourtDTO.getResult().getContactName())
                .laaOfficeAccount(magsCourtDTO.getResult().getLaaOfficeAccount())
                .legalAidWithdrawalDate(DateUtil.toDate(magsCourtDTO.getResult().getLegalAidWithdrawalDate()))
                .dateOfHearing(DateUtil.toDate(magsCourtDTO.getSession().getDateOfHearing()))
                .courtLocation(magsCourtDTO.getSession().getCourtLocation())
                .sessionValidateDate(DateUtil.toDate(magsCourtDTO.getSession().getSessionvalidateddate()))
                .build();

        wqResultRepository.save(wqResult);
    }


}
