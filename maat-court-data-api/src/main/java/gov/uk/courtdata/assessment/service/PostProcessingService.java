package gov.uk.courtdata.assessment.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.model.assessment.PostProcessing;
import gov.uk.courtdata.repository.PostProcessingStoredProcedureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@XRayEnabled
@RequiredArgsConstructor
public class PostProcessingService {

    private final PostProcessingStoredProcedureRepository postProcessingStoredProcedureRepository;

    @Transactional
    public void execute(PostProcessing postProcessing) {
        try {
            postProcessingStoredProcedureRepository.invokePostAssessmentProcessingCma(postProcessing);
        } catch (DataAccessException exception) {
            log.error("Post-processing failed for repId: {}", postProcessing.getRepId());
            throw exception;
        }
    }
}
