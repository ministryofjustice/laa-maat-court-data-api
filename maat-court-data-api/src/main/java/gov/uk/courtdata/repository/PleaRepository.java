package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.PleaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PleaRepository extends JpaRepository<PleaEntity,Integer> {
}