package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.CorrespondenceEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorrespondenceRepository extends JpaRepository<CorrespondenceEntity, Integer> {

    List<CorrespondenceEntity> findAllByRepId(Integer repId);
}
