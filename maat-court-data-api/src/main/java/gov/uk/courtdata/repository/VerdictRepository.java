package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.VerdictEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VerdictRepository extends JpaRepository<VerdictEntity,Integer> {

    @Query(value = "SELECT * FROM MLA.XXMLA_VERDICT WHERE OFFENCE_ID = :offenceId " +
            "AND TX_ID = (SELECT MAX( TX_ID ) FROM MLA.XXMLA_VERDICT WHERE OFFENCE_ID = :offenceId) ",
            nativeQuery = true)
    Optional<VerdictEntity> getLatestVerdictByOffence(String offenceId);

}