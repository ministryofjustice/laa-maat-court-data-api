package gov.uk.courtdata.integration;

import gov.uk.courtdata.entity.NewWorkReasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MockNewWorkReasonRepository extends JpaRepository<NewWorkReasonEntity, String> {
}
