package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepOrderCPDataRepository extends JpaRepository<RepOrderCPDataEntity, Integer> {

    Optional<RepOrderCPDataEntity> findByrepOrderId(Integer repOrderId);

}
