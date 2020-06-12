package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.XLATResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface XLATResultRepository extends JpaRepository<XLATResultEntity,Integer> {
}
