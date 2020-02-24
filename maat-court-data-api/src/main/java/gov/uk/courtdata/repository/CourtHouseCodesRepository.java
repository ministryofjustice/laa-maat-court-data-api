package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.CourtHouseCodesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface CourtHouseCodesRepository extends JpaRepository<CourtHouseCodesEntity, String> {

    /**
     *
     * @param code
     * @return
     */
    @Query(value = "SELECT COUNT(*) FROM MLA.XXMLA_XLAT_COURTHOUSE_CODES WHERE CODE =?1", nativeQuery = true)
    Integer getCount(String code);

}
