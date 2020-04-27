package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.WQSession;
import gov.uk.courtdata.hearing.magistrate.dto.MagistrateCourtDTO;
import gov.uk.courtdata.repository.WQSessionRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WQSessionProcessor {

    private final WQSessionRepository wqSessionRepository;

    public void process(MagistrateCourtDTO magsCourtDTO) {

        WQSession wqSession = WQSession.builder()
                .caseId(magsCourtDTO.getCaseId())
                .txId(magsCourtDTO.getTxId())
                .dateOfHearing(DateUtil.toDate(magsCourtDTO.getSession().getDateOfHearing()))
                .courtLocation(magsCourtDTO.getSession().getCourtLocation())
                .postHearingCustody(magsCourtDTO.getSession().getPostHearingCustody())
                .sessionvalidatedate(DateUtil.toDate(magsCourtDTO.getSession().getSessionvalidateddate()))
                .build();

        wqSessionRepository.save(wqSession);

    }

}
