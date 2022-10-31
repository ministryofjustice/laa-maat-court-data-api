package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.ContributionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContributionsRepository extends JpaRepository<ContributionsEntity, Integer> {

    Integer countAllByRepId(Integer repId);

    List<ContributionsEntity> findAllByRepId(Integer repId);
}