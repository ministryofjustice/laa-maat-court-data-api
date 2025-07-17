package gov.uk.courtdata.billing.repository;

import gov.uk.courtdata.billing.entity.BillingApplicantUpdateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BillingApplicantUpdateRepository extends JpaRepository<BillingApplicantUpdateEntity, Integer> {

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE TOGDATA.APPLICANTS
        SET     SEND_TO_CCLF = NULL,
                DATE_MODIFIED = CURRENT_TIMESTAMP,
                USER_MODIFIED = :username
        WHERE ID IN (:applicantIds)
        """, nativeQuery = true)
    int resetApplicantBilling(List<Integer> applicantIds, String username);
}