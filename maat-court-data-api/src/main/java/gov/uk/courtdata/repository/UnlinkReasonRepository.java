package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.UnlinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnlinkReasonRepository extends JpaRepository<UnlinkEntity, Integer> {
}
