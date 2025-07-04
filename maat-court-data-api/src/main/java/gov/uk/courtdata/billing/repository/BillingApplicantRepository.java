package gov.uk.courtdata.billing.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import gov.uk.courtdata.billing.entity.BillingApplicantEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingApplicantRepository extends JpaRepository<BillingApplicantEntity, Integer> {

    @Query(value = """
            SELECT a1.id, a1.first_name, a1.last_name, a1.other_names, a1.dob, a1.gender,
            a1.ni_number, a1.foreign_id, a1.date_created, a1.user_created,
            a1.date_modified, a1.user_modified
            FROM TOGDATA.applicants a1
            JOIN TOGDATA.maat_refs_to_extract m ON a1.id = m.appl_id
            UNION
            SELECT a2.id, a2.first_name, a2.last_name, a2.other_names, a2.dob, a2.gender,
            a2.ni_number, a2.foreign_id, a2.date_created, a2.user_created,
            a2.date_modified, a2.user_modified
            FROM TOGDATA.applicants a2
            WHERE a2.send_to_cclf = 'Y'
            """,
            nativeQuery = true)
    List<BillingApplicantEntity> findAllApplicantsForBilling();
}

