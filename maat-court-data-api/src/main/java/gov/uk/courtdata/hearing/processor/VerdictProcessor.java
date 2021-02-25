package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.VerdictEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.hearing.dto.VerdictDTO;
import gov.uk.courtdata.repository.VerdictRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class VerdictProcessor {

    private final VerdictRepository verdictRepository;

    public void process(final HearingDTO hearingDTO) {

        if (hearingDTO.getOffence().getVerdict()!=null) {

            VerdictDTO verdictDTO = hearingDTO.getOffence().getVerdict();
            VerdictEntity verdictEntity = VerdictEntity
                    .builder()
                    .txId(hearingDTO.getTxId())
                    .caseId(hearingDTO.getCaseId())
                    .maatId(hearingDTO.getMaatId())
                    .offenceId(verdictDTO.getOffenceId())
                    .verdictDate(DateUtil.parse(verdictDTO.getVerdictDate()))
                    .category(verdictDTO.getCategory())
                    .categoryType(verdictDTO.getCategoryType().name())
                    .cjsVerdictCode(verdictDTO.getCjsVerdictCode())
                    .verdictCode(verdictDTO.getVerdictCode())
                    .createdOn(LocalDateTime.now())
                    .build();

            verdictRepository.save(verdictEntity);
            log.info("Saved verdict successfully");
        }
    }
}