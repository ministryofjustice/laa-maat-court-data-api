package gov.uk.courtdata.hearing.processor;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.model.Result;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.IdentifierRepository;
import gov.uk.courtdata.repository.WQHearingRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@XRayEnabled
@RequiredArgsConstructor
public class WQHearingProcessor {

    private final WQHearingRepository wQHearingRepository;

    private final WqLinkRegisterRepository wqLinkRegisterRepository;
    private final IdentifierRepository identifierRepository;

    public void process(final HearingResulted hearingResulted) {

        List<WqLinkRegisterEntity> wqLinkRegisterEntities =
                wqLinkRegisterRepository.findBymaatId(hearingResulted.getMaatId());

        WqLinkRegisterEntity wqLinkReg = wqLinkRegisterEntities.iterator().next();

        WQHearingEntity wqHearingEntity = WQHearingEntity.builder()
                .caseId(wqLinkReg.getCaseId())
                .txId(identifierRepository.getTxnID())
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
}