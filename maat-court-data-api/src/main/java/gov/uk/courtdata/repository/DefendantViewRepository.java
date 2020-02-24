package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *
 */
@Repository
public interface DefendantViewRepository extends JpaRepository<DefendantMAATDataEntity,Integer> {

    /**
     *
     * @param maatId
     * @return
     */
    @Query("SELECT d FROM DefendantMAATDataEntity d WHERE d.maatId = ?1")
    Optional<DefendantMAATDataEntity> findByMaatId(final Integer maatId);

}
