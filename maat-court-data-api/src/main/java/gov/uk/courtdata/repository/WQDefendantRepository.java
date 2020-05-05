package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.WQDefendant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WQDefendantRepository extends JpaRepository<WQDefendant, Integer> {
}
