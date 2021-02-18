package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.WQPleaEntity;
import gov.uk.courtdata.entity.WQResultEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WQPleaRepository;
import gov.uk.courtdata.repository.WQResultRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WQPleaProcessor {

    private final WQPleaRepository wqPleaRepository;

    public void process(final HearingDTO hearingDTO) {

        WQPleaEntity wqPleaEntity = WQPleaEntity
                .builder()
                .offenceId(hearingDTO.getPleaDTO().getOffenceId())
                .pleaDate(hearingDTO.getPleaDTO().getPleaDate())
                .pleaValue(hearingDTO.getPleaDTO().getPleaValue())
                .build();

        wqPleaRepository.save(wqPleaEntity);
    }
}
