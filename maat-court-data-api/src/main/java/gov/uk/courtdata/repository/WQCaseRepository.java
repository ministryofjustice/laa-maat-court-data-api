package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.WQCaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WQCaseRepository extends JpaRepository<WQCaseEntity,Integer> {
}
