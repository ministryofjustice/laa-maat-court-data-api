package gov.uk.courtdata.applicant.repository;

import gov.uk.courtdata.applicant.entity.RepOrderApplicantLinksEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepOrderApplicantLinksRepository extends JpaRepository<RepOrderApplicantLinksEntity, Integer> {

    List<RepOrderApplicantLinksEntity> findAllByRepId(Integer repId);
    
    Optional<RepOrderApplicantLinksEntity> findFirstByRepIdAndLinkDateIsNotNullAndUnlinkDateIsNull(Integer repId);

}