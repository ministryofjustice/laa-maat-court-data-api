package gov.uk.courtdata.repository;

import gov.uk.courtdata.model.assessment.PostProcessing;
import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PostProcessingStoredProcedureRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void invokePostAssessmentProcessingCma(PostProcessing postProcessing) {
        final Session session = entityManager.unwrap(Session.class);
        final ProcedureCall postAssessmentProcessingProcedure =
                session.getNamedProcedureCall("post_assessment_processing_cma");
        postAssessmentProcessingProcedure.setParameter("p_rep_id", postProcessing.getRepId());
        postAssessmentProcessingProcedure.setParameter("p_user_name", postProcessing.getUser().getUsername());
        postAssessmentProcessingProcedure.setParameter("p_user_session", postProcessing.getUser().getId());
        postAssessmentProcessingProcedure.execute();
    }
}
