package gov.uk.courtdata.offence.helper;

import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.enums.WQType;
import gov.uk.courtdata.offence.model.OffenceSummary;
import gov.uk.courtdata.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static gov.uk.courtdata.constants.CourtDataConstants.*;


@RequiredArgsConstructor
@Component
public class OffenceHelper {

    private final OffenceRepository offenceRepository;
    private final WQResultRepository wqResultRepository;
    private final ResultRepository resultRepository;
    private final XLATResultRepository xlatResultRepository;
    private final WQOffenceRepository wqOffenceRepository;
    private final WqLinkRegisterRepository wqLinkRegisterRepository;


    public List<OffenceSummary> getTrialOffences(List<OffenceSummary> offenceList, int maatId) {

        int caseId = wqLinkRegisterRepository.findBymaatId(maatId).get(0).getCaseId();

        List<OffenceEntity> offenceEntities = offenceRepository.findByCaseId(caseId);
        List<Integer> committalForTrialRefResults = xlatResultRepository.findResultsByWQType(WQType.COMMITTAL_QUEUE.value(),
                COMMITTAL_FOR_TRIAL_SUB_TYPE);
        List<Integer> committalForSentenceRefResults = xlatResultRepository.findResultsByWQType(WQType.COMMITTAL_QUEUE.value(),
                COMMITTAL_FOR_SENTENCE_SUB_TYPE);

        return offenceList
                .stream()
                .filter(offence -> (hasCommittalResults(offence, offenceEntities, committalForTrialRefResults))
                        || isNewCCOffence(offence, offenceEntities, committalForSentenceRefResults, caseId))
                .collect(Collectors.toList());

    }


    private boolean isNewCCOffence(OffenceSummary offence, List<OffenceEntity> offenceEntities, List<Integer> committalForSentenceRefResults, int caseId) {
        boolean isNewCCOffence = false;

        int newOffenceCount = offenceRepository.getNewOffenceCount(caseId, offence.getOffenceId().toString())
                + wqOffenceRepository.getNewOffenceCount(caseId, offence.getOffenceId().toString());

        if (newOffenceCount > 0 &&
                !hasCommittalResults(offence, offenceEntities, committalForSentenceRefResults)) {
            isNewCCOffence = true;
        }
        return isNewCCOffence;
    }


    private boolean hasCommittalResults(OffenceSummary offence, List<OffenceEntity> offenceEntities, List<Integer> committalRefResults) {
        OffenceEntity offenceEntity = offenceEntities
                .stream()
                .filter(o -> o.getOffenceId() != null
                        && o.getOffenceId().equalsIgnoreCase(offence.getOffenceId().toString()))
                .findFirst().orElse(null);

        return hasResults(offenceEntity, committalRefResults);
    }


    private boolean hasResults(OffenceEntity offenceEntity, List<Integer> committalRefResults) {
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


    public boolean isNewOffence(Integer caseId, String asaSeq) {

        Integer offenceCount =
                offenceRepository.getOffenceCountForAsnSeq(
                        caseId,
                        String.format(LEADING_ZERO_3, Integer.parseInt(asaSeq)));

        return offenceCount == 0;
    }
}
