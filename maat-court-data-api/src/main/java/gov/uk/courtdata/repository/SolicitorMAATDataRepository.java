package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolicitorMAATDataRepository extends JpaRepository<SolicitorMAATDataEntity, Integer> {

    Optional<SolicitorMAATDataEntity> findBymaatId(Integer maatId);

}
