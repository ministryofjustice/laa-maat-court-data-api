package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.WQVerdictEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WQVerdictRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class WQVerdictProcessor {

    private final WQVerdictRepository wqVerdictRepository;

    public void process(final HearingDTO hearingDTO) {

        WQVerdictEntity wqVerdictEntity = WQVerdictEntity
                .builder()
                .verdictId(new Random().nextInt(100000))
                .offenceId(hearingDTO.getOffence().getVerdict().getOffenceId())
                .verdictDate(DateUtil.parse(hearingDTO.getOffence().getVerdict().getVerdictDate()))
                .category(hearingDTO.getOffence().getVerdict().getCategory())
                .categoryType(hearingDTO.getOffence().getVerdict().getCategoryType().name())
                .cjsVerdictCode(hearingDTO.getOffence().getVerdict().getCjsVerdictCode())
                .verdictCode(hearingDTO.getOffence().getVerdict().getVerdictCode())
                .createdOn(LocalDateTime.now())
                .build();

        wqVerdictRepository.save(wqVerdictEntity);
    }
}

