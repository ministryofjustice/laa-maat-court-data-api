package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.WQResultEntity;
import gov.uk.courtdata.entity.WQVerdictEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WQVerdictRepository extends JpaRepository<WQVerdictEntity,Integer> {
}
