package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.PleaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PleaRepository extends JpaRepository<PleaEntity, Integer> {

    @Query(value = "SELECT * FROM MLA.XXMLA_PLEA WHERE OFFENCE_ID = :offenceId " +
            "AND TX_ID = (SELECT MAX( TX_ID ) FROM MLA.XXMLA_PLEA WHERE OFFENCE_ID = :offenceId) ",
            nativeQuery = true)
    Optional<PleaEntity> getLatestPleaByOffence(String offenceId);
}