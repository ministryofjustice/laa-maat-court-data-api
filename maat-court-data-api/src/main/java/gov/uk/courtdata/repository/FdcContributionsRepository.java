package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.FdcContributionsEntity;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FdcContributionsRepository extends JpaRepository<FdcContributionsEntity, Integer> {
    List<FdcContributionsEntity> findByStatus(FdcContributionsStatus status);
    List<FdcContributionsEntity> findByIdIn(Set<Integer> ids);
}