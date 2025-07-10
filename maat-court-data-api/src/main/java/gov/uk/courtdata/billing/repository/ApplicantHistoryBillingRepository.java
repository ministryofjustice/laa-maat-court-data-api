package gov.uk.courtdata.billing.repository;

import gov.uk.courtdata.billing.entity.ApplicantHistoryBillingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicantHistoryBillingRepository extends JpaRepository<ApplicantHistoryBillingEntity, Integer> {
    @Query(value = """
                        SELECT 
                            A1.ID,
                            A1.APPL_ID,
                            A1.AS_AT_DATE,
                            A1.FIRST_NAME,
                            A1.LAST_NAME,
                            A1.OTHER_NAMES,
                            A1.DOB,
                            A1.GENDER,
                            A1.NI_NUMBER,
                            A1.FOREIGN_ID,
                            A1.DATE_CREATED,
                            A1.USER_CREATED,
                            A1.DATE_MODIFIED,
                            A1.USER_MODIFIED
                        FROM TOGDATA.APPLICANT_HISTORY A1
                        INNER JOIN TOGDATA.MAAT_REFS_TO_EXTRACT M
                        ON A1.ID = M.APHI_ID
                        UNION
                        SELECT 
                            A2.ID,
                            A2.APPL_ID,
                            A2.AS_AT_DATE,
                            A2.FIRST_NAME,
                            A2.LAST_NAME,
                            A2.OTHER_NAMES,
                            A2.DOB,
                            A2.GENDER,
                            A2.NI_NUMBER,
                            A2.FOREIGN_ID,
                            A2.DATE_CREATED,
                            A2.USER_CREATED,
                            A2.DATE_MODIFIED,
                            A2.USER_MODIFIED
                        FROM TOGDATA.APPLICANT_HISTORY A2
                        WHERE A2.SEND_TO_CCLF = 'Y'
                        FETCH FIRST 10 ROWS ONLY
                        """, nativeQuery = true)
    List<ApplicantHistoryBillingEntity> extractApplicantHistoryBilling();
}
