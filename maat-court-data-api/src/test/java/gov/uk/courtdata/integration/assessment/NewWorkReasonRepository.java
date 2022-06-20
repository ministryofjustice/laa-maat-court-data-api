package gov.uk.courtdata.integration.assessment;

import gov.uk.courtdata.entity.NewWorkReasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewWorkReasonRepository extends JpaRepository<NewWorkReasonEntity, String> {
}
