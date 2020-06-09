package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.WQSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WQSessionRepository extends JpaRepository<WQSessionEntity,Integer> {
}
