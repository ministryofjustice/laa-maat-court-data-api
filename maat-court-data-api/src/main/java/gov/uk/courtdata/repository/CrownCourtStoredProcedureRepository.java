package gov.uk.courtdata.repository;

import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class CrownCourtStoredProcedureRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void updateCrownCourtOutcome(Integer repId,
                                        String ccOutcome,
                                        String benchWarrantIssued,
                                        String appealType,
                                        String imprisoned,
                                        String caseNumber,
                                        String crownCourtCode) {


        final Session session = entityManager.unwrap(Session.class);

        final ProcedureCall ccOutcomeProcedure = session.getNamedProcedureCall("update_cc_outcome");

        ccOutcomeProcedure.getParameterRegistration("p_imprisoned").enablePassingNulls(true);
        ccOutcomeProcedure.getParameterRegistration("p_bench_warrant_issued").enablePassingNulls(true);
        ccOutcomeProcedure.getParameterRegistration("p_appeal_type").enablePassingNulls(true);

        ccOutcomeProcedure.setParameter("p_rep_id", repId);
        ccOutcomeProcedure.setParameter("p_imprisoned", imprisoned);
        ccOutcomeProcedure.setParameter("p_cc_outcome", ccOutcome);
        ccOutcomeProcedure.setParameter("p_bench_warrant_issued", benchWarrantIssued);
        ccOutcomeProcedure.setParameter("p_appeal_type", appealType);
        ccOutcomeProcedure.setParameter("p_case_number", caseNumber);
        ccOutcomeProcedure.setParameter("p_crown_court_code", crownCourtCode);


        ccOutcomeProcedure.execute();

    }
}
