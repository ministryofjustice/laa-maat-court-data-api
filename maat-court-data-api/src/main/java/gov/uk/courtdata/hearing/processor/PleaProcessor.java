package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.PleaEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.PleaRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PleaProcessor {

    private final PleaRepository pleaRepository;

    public void process(final HearingDTO hearingDTO) {

        PleaEntity pleaEntity = PleaEntity
                .builder()
                .txId(hearingDTO.getTxId())
                .caseId(hearingDTO.getCaseId())
                .maatId(hearingDTO.getMaatId())
                .offenceId(hearingDTO.getOffence().getPlea().getOffenceId())
                .pleaDate(DateUtil.parse(hearingDTO.getOffence().getPlea().getPleaDate()))
                .pleaValue(hearingDTO.getOffence().getPlea().getPleaValue())
                .createdOn(LocalDateTime.now())
                .build();

        pleaRepository.save(pleaEntity);
    }
}