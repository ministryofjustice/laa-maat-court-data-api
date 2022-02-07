package gov.uk.courtdata.prosecutionconcluded.helper;

import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.enums.WQType;
import gov.uk.courtdata.prosecutionconcluded.model.OffenceSummary;

import gov.uk.courtdata.repository.OffenceRepository;
import gov.uk.courtdata.repository.ResultRepository;
import gov.uk.courtdata.repository.WQResultRepository;
import gov.uk.courtdata.repository.XLATResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static gov.uk.courtdata.constants.CourtDataConstants.COMMITTAL_FOR_TRIAL_SUB_TYPE;


@RequiredArgsConstructor
@Component
public class OffenceHelper {

    private final OffenceRepository offenceRepository;
    private final WQResultRepository wqResultRepository;
    private final ResultRepository resultRepository;
    private final XLATResultRepository xlatResultRepository;


    public List<OffenceSummary> getTrialOffences(List<OffenceSummary> offenceList, int caseId) {

        List<OffenceEntity> offenceEntities = offenceRepository.findByCaseId(caseId);
        List<Integer> committalRefResults = xlatResultRepository.findResultsByWQType(WQType.COMMITTAL_QUEUE.value(),
                COMMITTAL_FOR_TRIAL_SUB_TYPE);

        return offenceList
                .stream()
                .filter(offence -> isTrialOffence(offence, offenceEntities, committalRefResults))
                .collect(Collectors.toList());

    }

    private boolean isTrialOffence(OffenceSummary offence, List<OffenceEntity> offenceEntities, List<Integer> committalRefResults) {
        Optional<OffenceEntity> optionalOffenceEntity = offenceEntities
                .stream()
                .filter(o -> o.getOffenceId() != null
                        && o.getOffenceId().equalsIgnoreCase(offence.getOffenceId().toString()))
                .findFirst();

        OffenceEntity offenceEntity = optionalOffenceEntity.orElse(null);


        return hasCommittalForTrailResults(offenceEntity, committalRefResults);
    }

    private boolean hasCommittalForTrailResults(OffenceEntity offenceEntity, List<Integer> committalRefResults) {
        boolean isCommittal = false;
        if (offenceEntity != null) {
            String asnSeq = getAsnSeq(offenceEntity);
            List<Integer> resultList = resultRepository
                    .findResultCodeByCaseIdAndAsnSeq(offenceEntity.getCaseId(), asnSeq);
            List<Integer> wqResultList = wqResultRepository
                    .findResultCodeByCaseIdAndAsnSeq(offenceEntity.getCaseId(), asnSeq);

            isCommittal = Stream.concat(resultList.stream(), wqResultList.stream())
                    .anyMatch(committalRefResults::contains);
        }
        return isCommittal;
    }

    private String getAsnSeq(OffenceEntity offenceEntity) {
        int asnSeq = Integer.parseInt(offenceEntity.getAsnSeq());
        return asnSeq < 9 ? offenceEntity.getAsnSeq().substring(2) : offenceEntity.getAsnSeq().substring(1);
    }


}
