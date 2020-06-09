package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.XLATOffenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface XLATOffenceRepository extends JpaRepository<XLATOffenceEntity,String> {
}
