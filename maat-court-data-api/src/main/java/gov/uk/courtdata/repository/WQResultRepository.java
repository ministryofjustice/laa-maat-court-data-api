package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.WQResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WQResultRepository extends JpaRepository<WQResultEntity,Integer> {
}
