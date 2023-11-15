package gov.uk.courtdata.applicant.repository;

import gov.uk.courtdata.applicant.entity.ApplicantHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantHistoryRepository extends JpaRepository<ApplicantHistoryEntity, Integer> {
}