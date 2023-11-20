package gov.uk.courtdata.eform.repository;

import gov.uk.courtdata.eform.repository.entity.EformResultsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EformResultsRepository extends JpaRepository<EformResultsEntity, Long> {

    Optional<EformResultsEntity> findByUsn(Integer usn);
    void deleteAllByUsn(Integer usn);

    EformResultsEntity findTopByUsnOrderByIdDesc(Integer usn);
}