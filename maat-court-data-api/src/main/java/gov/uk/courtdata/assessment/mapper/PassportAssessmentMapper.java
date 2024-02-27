package gov.uk.courtdata.assessment.mapper;

import gov.uk.courtdata.dto.PassportAssessmentDTO;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.model.assessment.CreatePassportAssessment;
import gov.uk.courtdata.model.assessment.UpdatePassportAssessment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PassportAssessmentMapper {

    @Mapping(target = "valid", source = "valid", defaultValue = "Y")
    PassportAssessmentDTO updatePassportAssessmentToPassportAssessmentDTO(final UpdatePassportAssessment assessment);

    @Mapping(target = "valid", source = "valid", defaultValue = "Y")
    PassportAssessmentDTO createPassportAssessmentToPassportAssessmentDTO(final CreatePassportAssessment assessment);

    @Mapping(target = "repId", source = "repOrder.id")
    PassportAssessmentDTO passportAssessmentEntityToPassportAssessmentDTO(final PassportAssessmentEntity passportAssessment);

    @Mapping(target = "repOrder.id", source = "repId")
    PassportAssessmentEntity passportAssessmentDtoToPassportAssessmentEntity(final PassportAssessmentDTO passportAssessment);


}