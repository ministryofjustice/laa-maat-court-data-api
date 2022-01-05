package gov.uk.courtdata.assessment.mapper;

import gov.uk.courtdata.dto.PassportAssessmentDTO;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.model.assessment.CreatePassportAssessment;
import gov.uk.courtdata.model.assessment.UpdatePassportAssessment;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PassportAssessmentMapper {

    PassportAssessmentDTO passportAssessmentEntityToPassportAssessmentDTO(final PassportAssessmentEntity passportAssessment);

    PassportAssessmentDTO updatePassportAssessmentToPassportAssessmentDTO(final UpdatePassportAssessment assessment);

    PassportAssessmentDTO createPassportAssessmentToPassportAssessmentDTO(final CreatePassportAssessment assessment);

    PassportAssessmentEntity passportAssessmentDtoToPassportAssessmentEntity(final PassportAssessmentDTO passportAssessment);

}