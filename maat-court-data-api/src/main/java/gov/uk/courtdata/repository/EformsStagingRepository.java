package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.EformsStagingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EformsStagingRepository extends JpaRepository<EformsStagingEntity,Integer> {


}
