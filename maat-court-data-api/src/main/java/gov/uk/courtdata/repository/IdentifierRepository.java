package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentifierRepository extends JpaRepository<WqLinkRegisterEntity, Long> {

    @Query(value = "select MLA.TXID.nextval from dual", nativeQuery = true)
    Integer getTxnID();

    @Query(value = "select MLA.CASEID.nextval from dual", nativeQuery = true)
    Integer getCaseID();

    @Query(value = "select MLA.PROCEDING.nextval from dual", nativeQuery = true)
    Integer getProceedingID();

    @Query(value = "select MLA.LIBRAID.nextval from dual", nativeQuery = true)
    Integer getLibraID();
}
