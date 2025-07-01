package gov.uk.courtdata.billing.repository;

import gov.uk.courtdata.billing.entity.MaatReferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MaatReferenceRepository extends JpaRepository<MaatReferenceEntity, Integer> {
    
    @Modifying
    @Query(value = "INSERT INTO TOGDATA.maat_refs_to_extract (MAAT_ID, APPL_ID, APHI_ID) SELECT id, appl_id, aphi_id FROM rep_orders WHERE send_to_cclf = 'Y'", nativeQuery = true)
    void populateMaatReferences();
}
