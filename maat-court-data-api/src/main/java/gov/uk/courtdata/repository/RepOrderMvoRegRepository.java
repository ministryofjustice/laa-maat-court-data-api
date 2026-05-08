package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.RepOrderMvoRegEntity;
import gov.uk.courtdata.reporder.projection.RepOrderMvoRegEntityInfo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RepOrderMvoRegRepository
        extends JpaRepository<RepOrderMvoRegEntity, Integer>, JpaSpecificationExecutor<RepOrderMvoRegEntity> {
    List<RepOrderMvoRegEntityInfo> findByMvoIdAndAndDateDeletedIsNull(Integer mvoId);
}
