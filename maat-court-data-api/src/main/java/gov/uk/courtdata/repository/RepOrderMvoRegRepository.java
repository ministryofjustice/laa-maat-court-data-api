package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.RepOrderMvoRegEntity;
import gov.uk.courtdata.reporder.projection.RepOrderMvoRegEntityInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepOrderMvoRegRepository extends JpaRepository<RepOrderMvoRegEntity, Integer>, JpaSpecificationExecutor<RepOrderMvoRegEntity> {
    List<RepOrderMvoRegEntityInfo> findByMvoIdAndAndDateDeletedIsNull(Integer mvoId);
}