package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.PleaEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.hearing.dto.PleaDTO;
import gov.uk.courtdata.repository.PleaRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class PleaProcessor {

    private final PleaRepository pleaRepository;

    public void process(final HearingDTO hearingDTO) {

        if (hearingDTO.getOffence().getPlea()!=null) {

            PleaDTO plea = hearingDTO.getOffence().getPlea();
            PleaEntity pleaEntity = PleaEntity
                    .builder()
                    .txId(hearingDTO.getTxId())
                    .caseId(hearingDTO.getCaseId())
                    .maatId(hearingDTO.getMaatId())
                    .offenceId(plea.getOffenceId())
                    .pleaDate(DateUtil.parse(plea.getPleaDate()))
                    .pleaValue(plea.getPleaValue())
                    .createdOn(LocalDateTime.now())
                    .build();

            pleaRepository.save(pleaEntity);
            log.info("Saved plea successfully");
        }
    }
}