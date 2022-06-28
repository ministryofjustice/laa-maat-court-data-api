package gov.uk.courtdata.hearing.processor;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.entity.WQCaseEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WQCaseRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static gov.uk.courtdata.constants.CourtDataConstants.LEADING_ZERO_2;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Component
@XRayEnabled
@RequiredArgsConstructor
public class WQCaseProcessor {

    private final WQCaseRepository wqCaseRepository;

    /**
     * @param magsCourtDTO
     */
    public void process(final HearingDTO magsCourtDTO) {

        WQCaseEntity wqCaseEntity = WQCaseEntity.builder().caseId(magsCourtDTO.getCaseId())
                .txId(magsCourtDTO.getTxId())
                .asn(magsCourtDTO.getAsn())
                .docLanguage(magsCourtDTO.getDocLanguage())
                .inactive(magsCourtDTO.getInActive())
                .libraCreationDate(getCreationDate(magsCourtDTO.getCaseCreationDate()))
                .cjsAreaCode(magsCourtDTO.getCjsAreaCode() != null ? String.format(LEADING_ZERO_2, Integer.parseInt(magsCourtDTO.getCjsAreaCode())) : null)
                .proceedingId(magsCourtDTO.getProceedingId())
                .build();
        wqCaseRepository.save(wqCaseEntity);
    }

    /**
     * Get the creation date in request format it else return system date.
     *
     * @param creationDate the given date
     * @return
     */
    private LocalDate getCreationDate(final String creationDate) {
        return isNotEmpty(creationDate) ? DateUtil.parse(creationDate) : LocalDate.now();
    }
}