package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.RepOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepOrderDataRepository extends JpaRepository<RepOrderEntity,Integer> {
}
