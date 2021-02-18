package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.WQResultEntity;
import gov.uk.courtdata.entity.WQVerdictEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.hearing.dto.OffenceDTO;
import gov.uk.courtdata.repository.WQResultRepository;
import gov.uk.courtdata.repository.WQVerdictRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WQVerdictProcessor {

    private final WQVerdictRepository wqVerdictRepository;

    public void process(final HearingDTO hearingDTO) {

        WQVerdictEntity wqVerdictEntity = WQVerdictEntity
                .builder()
                .offenceId(hearingDTO.getVerdictDTO().getOffenceId())
                .verdictDate(hearingDTO.getVerdictDTO().getVerdictDate())
                .category(hearingDTO.getVerdictDTO().getCategory())
                .categoryType(hearingDTO.getVerdictDTO().getCategoryType())
                .cjsVerdictCode(hearingDTO.getVerdictDTO().getCjsVerdictCode())
                .verdictCode(hearingDTO.getVerdictDTO().getVerdictCode())
                .build();

        wqVerdictRepository.save(wqVerdictEntity);
    }
}
