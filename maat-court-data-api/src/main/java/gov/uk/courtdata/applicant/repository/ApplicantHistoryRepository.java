package gov.uk.courtdata.applicant.repository;

import gov.uk.courtdata.applicant.entity.ApplicantHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicantHistoryRepository extends JpaRepository<ApplicantHistoryEntity, Integer> {
    @Query(value = """
                        SELECT A1.*
                        FROM TOGDATA.APPLICANT_HISTORY A1
                        INNER JOIN TOGDATA.MAAT_REFS_TO_EXTRACT M
                        ON A1.ID = M.APHI_ID
                        UNION
                        SELECT A2.*
                        FROM TOGDATA.APPLICANT_HISTORY A2
                        WHERE A2.SEND_TO_CCLF = 'Y'
                        """, nativeQuery = true)
    List<ApplicantHistoryEntity> extractApplicantHistoryBilling();
}