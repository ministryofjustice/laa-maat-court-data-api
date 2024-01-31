package gov.uk.courtdata.applicant.repository;

import gov.uk.courtdata.applicant.entity.ApplicantDisabilitiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantDisabilitiesRepository extends JpaRepository<ApplicantDisabilitiesEntity, Integer> {
}
