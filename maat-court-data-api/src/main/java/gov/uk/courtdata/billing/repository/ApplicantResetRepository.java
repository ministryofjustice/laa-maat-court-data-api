package gov.uk.courtdata.billing.repository;

import gov.uk.courtdata.billing.entity.ApplicantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ApplicantResetRepository extends JpaRepository<ApplicantEntity, Integer> {

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE TOGDATA.APPLICANTS
        SET     SEND_TO_CCLF = NULL,
            DATE_MODIFIED = CURRENT_TIMESTAMP,
            USER_MODIFIED = :username
        WHERE ID IN (:applicantIds)
        """, nativeQuery = true)
    int resetApplicant(List<Integer> applicantIds, String username);
}