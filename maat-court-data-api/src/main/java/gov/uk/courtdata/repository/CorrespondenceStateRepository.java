package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.CorrespondenceStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorrespondenceStateRepository extends JpaRepository<CorrespondenceStateEntity, Integer> {

    CorrespondenceStateEntity findByRepId(Integer repId);

}
