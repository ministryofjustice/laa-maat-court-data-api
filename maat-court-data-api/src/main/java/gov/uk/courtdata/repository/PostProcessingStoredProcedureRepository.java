package gov.uk.courtdata.repository;

import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PostProcessingStoredProcedureRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void invokePostAssessmentProcessingCma(Integer repId) {
        final Session session = entityManager.unwrap(Session.class);
        final ProcedureCall postAssessmentProcessingProcedure =
                session.getNamedProcedureCall("post_assessment_processing_cma");
        postAssessmentProcessingProcedure.setParameter("p_rep_id", repId);
        postAssessmentProcessingProcedure.execute();
    }
}
