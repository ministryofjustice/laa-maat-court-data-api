package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.AppealTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppealTypeRepository extends JpaRepository<AppealTypeEntity, String> {
}
