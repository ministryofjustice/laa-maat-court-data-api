package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.DefendantMAATDataEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefendantMAATDataRepository extends JpaRepository<DefendantMAATDataEntity, Integer> {

    Optional<DefendantMAATDataEntity> findBymaatId(Integer maatId);
}
