package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.RepOrderCPDataEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepOrderCPDataRepository extends JpaRepository<RepOrderCPDataEntity, Integer> {

    Optional<RepOrderCPDataEntity> findByrepOrderId(Integer repOrderId);

    Optional<RepOrderCPDataEntity> findByDefendantId(String defendantId);
}
