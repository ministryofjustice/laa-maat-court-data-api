package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.RepOrderMvoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepOrderMvoRepository extends JpaRepository<RepOrderMvoEntity, Integer>, JpaSpecificationExecutor<RepOrderMvoEntity> {
}