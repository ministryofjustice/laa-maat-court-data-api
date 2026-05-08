package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.CrownCourtCode;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrownCourtCodeRepository extends JpaRepository<CrownCourtCode, String> {

    Optional<CrownCourtCode> findByOuCode(String ouCode);
}
