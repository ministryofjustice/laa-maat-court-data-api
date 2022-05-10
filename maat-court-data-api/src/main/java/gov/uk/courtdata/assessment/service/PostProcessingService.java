package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.repository.PostProcessingStoredProcedureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostProcessingService {

    private final PostProcessingStoredProcedureRepository postProcessingStoredProcedureRepository;

    @Transactional
    public void execute(Integer repId) {
        try {
            postProcessingStoredProcedureRepository.invokePostAssessmentProcessingCma(repId);
        } catch (DataAccessException exception) {
            log.error("Post-processing failed for repId: {}", repId);
            throw exception;
        }
    }
}
