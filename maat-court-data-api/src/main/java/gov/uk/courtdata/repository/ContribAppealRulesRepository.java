package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.ContribAppealRulesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContribAppealRulesRepository extends JpaRepository<ContribAppealRulesEntity, Integer> {

    @Query(value = "SELECT contrib_amount FROM TOGDATA.CONTRIB_APPEAL_RULES car WHERE car.CATY_CASE_TYPE = :caseType " +
            "AND car.APTY_CODE = :aptyCode AND car.CCOO_OUTCOME = :outcome AND car.ASSESSMENT_RESULT = :outcome)", nativeQuery = true)
    Integer findContributionAmount(@Param("caseType") String caseType, @Param("aptyCode") String appealType,
                                @Param("outcome") String outcome, @Param("result") String result);

}