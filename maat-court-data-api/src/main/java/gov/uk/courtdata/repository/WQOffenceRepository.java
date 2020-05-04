package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.WQOffence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WQOffenceRepository extends JpaRepository<WQOffence,Integer> {
}
