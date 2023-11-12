package gov.uk.courtdata.repository;

import gov.uk.courtdata.dces.enums.ConcorContributionStatus;
import gov.uk.courtdata.entity.ConcorContributionsEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConcorContributionsRepository extends JpaRepository<ConcorContributionsEntity, Integer> {
    List<ConcorContributionsEntity> findByStatus(ConcorContributionStatus status);
}