package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WQHearingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class WQHearingProcessor {

    private final WQHearingRepository wQHearingRepository;

    /**
     * @param hearingDTO
     */
    public void process(final HearingDTO hearingDTO) {

        WQHearingEntity wqHearingEntity = WQHearingEntity.builder()
                .txId(hearingDTO.getTxId())
                .caseId(hearingDTO.getCaseId())
                .hearingUUID(hearingDTO.getHearingId().toString())
                .maatId(hearingDTO.getMaatId())
                .wqJurisdictionType(getJurisdictionTypeFrom(hearingDTO))
                .ouCourtLocation(getOUCourtLocationFrom(hearingDTO))
                .build();

        wQHearingRepository.save(wqHearingEntity);

    }
    private String getJurisdictionTypeFrom(HearingDTO hearingDTO){
        return Objects.isNull(hearingDTO.getJurisdictionType()) ? null : hearingDTO.getJurisdictionType().toString();
    }
    private String getOUCourtLocationFrom(HearingDTO hearingDTO){
        if(Objects.isNull(hearingDTO.getSession()) || Objects.isNull(hearingDTO.getSession().getCourtLocation())) {
            return null;
        }
        return hearingDTO.getSession().getCourtLocation();
    }
}
