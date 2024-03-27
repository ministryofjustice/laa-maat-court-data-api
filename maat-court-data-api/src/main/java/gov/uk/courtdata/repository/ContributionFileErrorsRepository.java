package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.ContributionFileErrorsEntity;
import gov.uk.courtdata.model.id.ContributionFileErrorsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContributionFileErrorsRepository extends JpaRepository<ContributionFileErrorsEntity, ContributionFileErrorsId> {

}
