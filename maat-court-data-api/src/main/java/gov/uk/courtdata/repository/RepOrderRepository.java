package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.RepOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface RepOrderRepository extends JpaRepository<RepOrderEntity, Integer>, JpaSpecificationExecutor<RepOrderEntity> {

}
