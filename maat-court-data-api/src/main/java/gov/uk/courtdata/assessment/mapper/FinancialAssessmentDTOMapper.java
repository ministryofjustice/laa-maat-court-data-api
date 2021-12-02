package gov.uk.courtdata.assessment.mapper;

import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.model.assessment.CreateFinancialAssessment;
import gov.uk.courtdata.model.assessment.UpdateFinancialAssessment;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface FinancialAssessmentDTOMapper {

    FinancialAssessmentDTO toFinancialAssessmentDTO(final FinancialAssessmentEntity financialAssessment);

    FinancialAssessmentEntity toFinancialAssessmentEntity(final CreateFinancialAssessment financialAssessment);

    FinancialAssessmentEntity toFinancialAssessmentEntity(final UpdateFinancialAssessment financialAssessment);
}