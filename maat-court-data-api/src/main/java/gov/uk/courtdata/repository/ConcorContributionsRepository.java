package gov.uk.courtdata.repository;

import gov.uk.courtdata.enums.ConcorContributionStatus;
import gov.uk.courtdata.entity.ConcorContributionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

@Repository
public interface ConcorContributionsRepository extends JpaRepository<ConcorContributionsEntity, Integer> {
    List<ConcorContributionsEntity> findByStatus(ConcorContributionStatus status);
    List<ConcorContributionsEntity> findByIdIn(Set<String> ids);
}