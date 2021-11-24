package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.WQHearingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WQHearingRepository extends JpaRepository<WQHearingEntity, String> {

}