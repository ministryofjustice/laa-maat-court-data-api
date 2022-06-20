package gov.uk.courtdata.hearing.processor;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.entity.WQSessionEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WQSessionRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Component
@XRayEnabled
@RequiredArgsConstructor
public class WQSessionProcessor {

    private final WQSessionRepository wqSessionRepository;

    /**
     * @param magsCourtDTO
     */
    public void process(HearingDTO magsCourtDTO) {

        WQSessionEntity wqSessionEntity = WQSessionEntity.builder()
                .caseId(magsCourtDTO.getCaseId())
                .txId(magsCourtDTO.getTxId())
                .dateOfHearing(DateUtil.parse(magsCourtDTO.getSession().getDateOfHearing()))
                .courtLocation(magsCourtDTO.getSession().getCourtLocation())
                .postHearingCustody(magsCourtDTO.getSession().getPostHearingCustody())
                .sessionvalidatedate(getSessionDate(magsCourtDTO.getSession().getSessionValidatedDate()))
                .build();

        wqSessionRepository.save(wqSessionEntity);

    }

    /**
     * If the session date is in request format to standard  else return system date.
     *
     * @param sessionDate the given date
     * @return
     */
    private LocalDate getSessionDate(final String sessionDate) {
        return
                isNotEmpty(sessionDate) ? DateUtil.parse(sessionDate) : LocalDate.now();
    }

}
