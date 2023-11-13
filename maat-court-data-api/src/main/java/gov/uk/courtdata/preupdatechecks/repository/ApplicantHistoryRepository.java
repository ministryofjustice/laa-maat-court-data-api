package gov.uk.courtdata.preupdatechecks.repository;

import gov.uk.courtdata.preupdatechecks.entity.ApplicantHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantHistoryRepository extends JpaRepository<ApplicantHistoryEntity, Integer> {
}