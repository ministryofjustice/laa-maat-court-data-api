package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.CrownCourtCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrownCourtCodeRepository extends JpaRepository<CrownCourtCode, String> {

    Optional<CrownCourtCode> findByOuCode(String ouCode);


}
