package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.WQPleaEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WQPleaRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class WQPleaProcessor {

    private final WQPleaRepository wqPleaRepository;

    public void process(final HearingDTO hearingDTO) {

        WQPleaEntity wqPleaEntity = WQPleaEntity
                .builder()
                .pleaId(new Random().nextInt(100000))
                .offenceId(hearingDTO.getOffence().getPlea().getOffenceId())
                .pleaDate(DateUtil.parse(hearingDTO.getOffence().getPlea().getPleaDate()))
                .pleaValue(hearingDTO.getOffence().getPlea().getPleaValue())
                .createdOn(LocalDateTime.now())
                .build();

        wqPleaRepository.save(wqPleaEntity);
    }
}
