package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.CorrespondenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CorrespondenceRepository extends JpaRepository<CorrespondenceEntity, Integer> {

    List<CorrespondenceEntity> findAllByRepId(Integer repId);

}
