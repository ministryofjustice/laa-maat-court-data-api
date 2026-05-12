package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.SolicitorMAATDataEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitorMAATDataRepository extends JpaRepository<SolicitorMAATDataEntity, Integer> {

    Optional<SolicitorMAATDataEntity> findBymaatId(Integer maatId);
}
