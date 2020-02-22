package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DefendantMAATDataRepository extends JpaRepository<DefendantMAATDataEntity, Integer> {

    Optional<DefendantMAATDataEntity> findBymaatId(Integer maatId);

}
