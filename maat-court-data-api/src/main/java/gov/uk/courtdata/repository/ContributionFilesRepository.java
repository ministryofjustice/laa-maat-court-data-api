package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.ContributionFilesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContributionFilesRepository extends JpaRepository<ContributionFilesEntity, Integer> {

    @Query(value= "SELECT MAX(CC.id) FROM TOGDATA.CONTRIBUTION_FILES CC ", nativeQuery = true)
    Integer findMaxId();
}
