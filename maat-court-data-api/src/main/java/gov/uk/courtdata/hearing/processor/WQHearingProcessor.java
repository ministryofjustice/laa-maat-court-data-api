package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WQHearingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

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
                .wqJurisdictionType(getJurisdictionTypeFrom(hearingDTO))
                .ouLocation(getOULocationFrom(hearingDTO))
                .build();

        wQHearingRepository.save(wqHearingEntity);
    }
    private String getJurisdictionTypeFrom(HearingDTO hearingDTO){
        return Objects.isNull(hearingDTO.getJurisdictionType()) ? null : hearingDTO.getJurisdictionType().toString();
    }
    private String getOULocationFrom(HearingDTO hearingDTO){
        if(Objects.isNull(hearingDTO.getSession()) || Objects.isNull(hearingDTO.getSession().getCourtLocation())) {
            return null;
        }
        return hearingDTO.getSession().getCourtLocation();
    }
}
