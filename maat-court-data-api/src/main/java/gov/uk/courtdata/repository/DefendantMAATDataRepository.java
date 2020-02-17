package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DefendantMAATDataRepository extends JpaRepository<DefendantMAATDataEntity, Integer> {

    Optional<DefendantMAATDataEntity> findBymaatId(Integer maatId);

    @Query(value = "select MLA.TXID.nextval from dual", nativeQuery = true)
    Integer getTxnID();


    @Query(value = "select MLA.CASEID.nextval from dual", nativeQuery = true)
    Integer getCaseID();

    @Query(value = "select MLA.PROCEDING.nextval from dual", nativeQuery = true)
    Integer getProceedingID();
}
