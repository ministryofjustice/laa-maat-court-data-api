package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.WQCase;
import gov.uk.courtdata.hearing.magistrate.dto.MagistrateCourtDTO;
import gov.uk.courtdata.repository.WQCaseRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Component
@RequiredArgsConstructor
public class WQCaseProcessor {

    private final WQCaseRepository wqCaseRepository;

    /**
     * @param magsCourtDTO
     */
    public void process(final MagistrateCourtDTO magsCourtDTO) {


        WQCase wqCase = WQCase.builder().caseId(magsCourtDTO.getCaseId())
                .txId(magsCourtDTO.getTxId())
                .asn(magsCourtDTO.getAsn())
                .docLanguage(magsCourtDTO.getDocLanguage())
                .inactive(magsCourtDTO.getInActive())
                .libraCreationDate(getCreationDate(magsCourtDTO.getCaseCreationDate()))
                .cjsAreaCode(magsCourtDTO.getCjsAreaCode())
                .proceedingId(magsCourtDTO.getProceedingId())
                .build();
        wqCaseRepository.save(wqCase);
    }

    /**
     * Get the creation date in request format it else return system date.
     *
     * @param creationDate the given date
     * @return
     */
    private LocalDate getCreationDate(final String creationDate) {
        return
                isNotEmpty(creationDate) ? DateUtil.toDate(creationDate) : LocalDate.now();

    }


}
