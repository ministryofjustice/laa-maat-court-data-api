package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.WQPleaEntity;
import gov.uk.courtdata.entity.WQResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WQPleaRepository extends JpaRepository<WQPleaEntity,Integer> {
}
