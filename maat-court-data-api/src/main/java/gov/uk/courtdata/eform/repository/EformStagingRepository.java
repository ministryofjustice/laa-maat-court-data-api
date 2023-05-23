package gov.uk.courtdata.eform.repository;

import gov.uk.courtdata.eform.repository.entity.EformsStagingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EformStagingRepository extends JpaRepository<EformsStagingEntity,Integer> {
    
}
