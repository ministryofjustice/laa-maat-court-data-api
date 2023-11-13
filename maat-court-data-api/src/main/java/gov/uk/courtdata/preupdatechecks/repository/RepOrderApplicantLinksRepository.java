package gov.uk.courtdata.preupdatechecks.repository;

import gov.uk.courtdata.preupdatechecks.entity.RepOrderApplicantLinksEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepOrderApplicantLinksRepository extends JpaRepository<RepOrderApplicantLinksEntity, Integer> {
    List<RepOrderApplicantLinksEntity> findAllByRepId(Integer repId);

}