package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WQHearingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class WQHearingProcessor {

    private final WQHearingRepository wQHearingRepository;

    /**
     * @param hearingDTO
     */
    public void process(final HearingDTO hearingDTO) {

        WQHearingEntity wqHearingEntity = WQHearingEntity.builder()
                .hearingUUID(hearingDTO.getHearingId().toString())
                .maatId(hearingDTO.getMaatId())
                .wqJurisdictionType(hearingDTO.getJurisdictionType().toString())
                .createdDateTime(LocalDateTime.now())
                .build();

        wQHearingRepository.save(wqHearingEntity);
    }
}
