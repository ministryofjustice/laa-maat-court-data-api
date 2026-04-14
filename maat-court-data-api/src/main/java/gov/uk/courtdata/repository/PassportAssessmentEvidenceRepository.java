package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.entity.PassportAssessmentEvidenceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassportAssessmentEvidenceRepository extends
    JpaRepository<PassportAssessmentEvidenceEntity, Integer> {
        List<PassportAssessmentEvidenceEntity> findByPassportAssessment(PassportAssessmentEntity passportAssessment);
}
