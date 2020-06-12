package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.WQOffenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WQOffenceRepository extends JpaRepository<WQOffenceEntity,Integer> {
}
