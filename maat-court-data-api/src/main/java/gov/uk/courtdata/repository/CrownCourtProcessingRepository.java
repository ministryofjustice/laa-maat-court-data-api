package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.CrownCourtOutComeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CrownCourtProcessingRepository extends JpaRepository<CrownCourtOutComeEntity, Integer> {

    @Transactional
    @Procedure(procedureName = "togdata.application.update_cc_outcome")
    void invokeCrownCourtOutcomeProcess(
            @Param("p_rep_id") Integer repId,
            @Param("p_cc_outcome") String cc_outcome,
            @Param("p_bench_warrant_issued") String bench_warrant_issued,
            @Param("p_appeal_type") String appeal_type,
            @Param("p_imprisoned") String imprisoned,
            @Param("p_case_number") String case_number,
            @Param("p_crown_court_code") String crown_court_code);

}
