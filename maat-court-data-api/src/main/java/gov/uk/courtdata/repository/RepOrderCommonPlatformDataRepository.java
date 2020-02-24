package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.RepOrderCommonPlatformDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepOrderCommonPlatformDataRepository extends JpaRepository<RepOrderCommonPlatformDataEntity, Integer> {

    Optional<RepOrderCommonPlatformDataEntity> findByrepOrderId(Integer repOrderId);

}
