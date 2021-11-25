package gov.uk.courtdata.prosecutionconcluded.service;

import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.prosecutionconcluded.dto.ConcludedDTO;
import gov.uk.courtdata.prosecutionconcluded.helper.CrownCourtCodeHelper;
import gov.uk.courtdata.prosecutionconcluded.helper.ProcessSentencingHelper;
import gov.uk.courtdata.prosecutionconcluded.helper.ResultCodeHelper;
import gov.uk.courtdata.repository.CrownCourtStoredProcedureRepository;
import gov.uk.courtdata.repository.RepOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProsecutionConcludedDAO {

    private final RepOrderRepository repOrderRepository;

    private final CrownCourtStoredProcedureRepository crownCourtStoredProcedureRepository;

    private final CrownCourtCodeHelper crownCourtCodeHelper;

    private final ProcessSentencingHelper processSentencingHelper;

    private final ResultCodeHelper resultCodeHelper;

    public void execute(ConcludedDTO concludedDTO) {

        Integer maatId = concludedDTO.getProsecutionConcluded().getMaatId();
        final Optional<RepOrderEntity> optionalRepEntity = repOrderRepository.findById(maatId);

        if (optionalRepEntity.isPresent()) {

            RepOrderEntity repOrderEntity = optionalRepEntity.get();

            crownCourtStoredProcedureRepository
                    .updateCrownCourtOutcome(
                            maatId,
                            concludedDTO.getCalculatedOutcome(),
                            resultCodeHelper.isBenchWarrantIssued(concludedDTO.getCalculatedOutcome(), concludedDTO.getHearingResultCode()),
                            repOrderEntity.getAptyCode(),
                            resultCodeHelper.isImprisoned(concludedDTO.getCalculatedOutcome(), concludedDTO.getHearingResultCode()),
                            concludedDTO.getCaseUrn(),
                            crownCourtCodeHelper.getCode(concludedDTO.getOuCourtLocation()));

            processSentencingHelper.processSentencingDate(concludedDTO.getCaseEndDate(), maatId, "caseType");
        }
    }
}