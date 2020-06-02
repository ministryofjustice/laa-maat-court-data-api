package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.CrownCourtOutComeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CrownCourtOutcomeRepository extends JpaRepository<CrownCourtOutComeEntity, String> {

}
