package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.WQHearingEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WQHearingRepository extends JpaRepository<WQHearingEntity, Integer> {

    List<WQHearingEntity> findByMaatIdAndHearingUUID(Integer maatId, String hearingUuid);
}
