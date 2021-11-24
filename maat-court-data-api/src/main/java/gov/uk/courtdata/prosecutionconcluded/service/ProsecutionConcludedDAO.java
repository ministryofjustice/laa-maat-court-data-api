package gov.uk.courtdata.prosecutionconcluded.service;

import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.hearing.crowncourt.impl.CrownCourtProcessHelper;
import gov.uk.courtdata.prosecutionconcluded.dto.ConcludedDTO;
import gov.uk.courtdata.prosecutionconcluded.helper.CrownCourtCodeHelper;
import gov.uk.courtdata.prosecutionconcluded.helper.ProcessSentencingHelper;
import gov.uk.courtdata.repository.CrownCourtStoredProcedureRepository;
import gov.uk.courtdata.repository.RepOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProsecutionConcludedDAO {

    private final RepOrderRepository repOrderRepository;

    private final CrownCourtProcessHelper crownCourtProcessHelper;

    private final CrownCourtStoredProcedureRepository crownCourtStoredProcedureRepository;

    private final CrownCourtCodeHelper crownCourtCodeHelper;

    private final ProcessSentencingHelper processSentencingHelper;

    public void execute(ConcludedDTO concludedDTO) {
        String appealType = concludedDTO.getAppealType();
        Integer maatId = concludedDTO.getProsecutionConcluded().getMaatId();
        final Optional<RepOrderEntity> optionalRepEntity = repOrderRepository.findById(maatId);

        if (optionalRepEntity.isPresent()) {

            RepOrderEntity repOrderEntity = optionalRepEntity.get();

            crownCourtStoredProcedureRepository
                    .updateCrownCourtOutcome(
                            maatId,
                            concludedDTO.getCalculatedOutcome(),
                            //todo: need to calculate
                            crownCourtProcessHelper.isBenchWarrantIssued(null),
                            appealType != null ? appealType : repOrderEntity.getAptyCode(),
                            //todo: need to calculate
                            crownCourtProcessHelper.isImprisoned(null, concludedDTO.getCalculatedOutcome()),
                            concludedDTO.getCaseUrn(),
                            crownCourtCodeHelper.get(concludedDTO.getOuCourtLocation()));

            processSentencingHelper.processSentencingDate(concludedDTO.getCaseEndDate(), maatId, "caseType");
        }
    }
}
