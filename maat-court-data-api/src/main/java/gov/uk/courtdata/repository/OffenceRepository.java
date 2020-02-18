package gov.uk.courtdata.repository;


import gov.uk.courtdata.entity.OffenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffenceRepository extends JpaRepository<OffenceEntity,Integer> {

}
