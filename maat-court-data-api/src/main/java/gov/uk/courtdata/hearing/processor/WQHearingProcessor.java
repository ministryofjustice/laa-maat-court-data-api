package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.model.Result;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.WQHearingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class WQHearingProcessor {

    private final WQHearingRepository wQHearingRepository;

    public void process(final HearingResulted hearingResulted) {

        WQHearingEntity wqHearingEntity = WQHearingEntity.builder()
                .hearingUUID(hearingResulted.getHearingId().toString())
                .maatId(hearingResulted.getMaatId())
                .wqJurisdictionType(hearingResulted.getJurisdictionType().name())
                .ouCourtLocation(hearingResulted.getSession().getCourtLocation())
                .caseUrn(hearingResulted.getCaseUrn())
                .resultCodes(flattenResults(hearingResulted))
                .build();

        wQHearingRepository.save(wqHearingEntity);

    }

    private String flattenResults(final HearingResulted hearingResulted) {
         List<String> results = hearingResulted
                .getDefendant()
                .getOffences()
                .stream()
                .flatMap(offence -> offence.getResults().stream())
                .map(Result::getResultCode)
                .distinct()
                .collect(Collectors.toList());

         return String.join(",",results);
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
