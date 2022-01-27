package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.WQHearingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WQHearingRepository extends JpaRepository<WQHearingEntity, String> {

    Optional<WQHearingEntity> findByMaatIdAndHearingUUID(Integer maatId, String hearingUuid);

}