package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.VerdictEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.VerdictRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class VerdictProcessor {

    private final VerdictRepository verdictRepository;

    public void process(final HearingDTO hearingDTO) {

        VerdictEntity verdictEntity = VerdictEntity
                .builder()
                .txId(hearingDTO.getTxId())
                .caseId(hearingDTO.getCaseId())
                .maatId(hearingDTO.getMaatId())
                .offenceId(hearingDTO.getOffence().getVerdict().getOffenceId())
                .verdictDate(DateUtil.parse(hearingDTO.getOffence().getVerdict().getVerdictDate()))
                .category(hearingDTO.getOffence().getVerdict().getCategory())
                .categoryType(hearingDTO.getOffence().getVerdict().getCategoryType().name())
                .cjsVerdictCode(hearingDTO.getOffence().getVerdict().getCjsVerdictCode())
                .verdictCode(hearingDTO.getOffence().getVerdict().getVerdictCode())
                .createdOn(LocalDateTime.now())
                .build();

        verdictRepository.save(verdictEntity);
    }
}

