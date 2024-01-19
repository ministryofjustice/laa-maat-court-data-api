package gov.uk.courtdata.reporder.repository;

import gov.uk.courtdata.entity.RepOrderEquityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepOrderEquityRepository extends JpaRepository<RepOrderEquityEntity, Integer> {
}
