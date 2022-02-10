package gov.uk.courtdata.assessment.mapper;

import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.entity.ChildWeightingsEntity;
import gov.uk.courtdata.entity.FinancialAssessmentDetailsEntity;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.model.assessment.ChildWeightings;
import gov.uk.courtdata.model.assessment.CreateFinancialAssessment;
import gov.uk.courtdata.model.assessment.FinancialAssessmentDetails;
import gov.uk.courtdata.model.assessment.UpdateFinancialAssessment;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FinancialAssessmentMapper {

    FinancialAssessmentDTO FinancialAssessmentEntityToFinancialAssessmentDTO(final FinancialAssessmentEntity financialAssessment);

    FinancialAssessmentDTO UpdateFinancialAssessmentToFinancialAssessmentDTO(final UpdateFinancialAssessment assessment);

    FinancialAssessmentDTO CreateFinancialAssessmentToFinancialAssessmentDTO(final CreateFinancialAssessment assessment);

    FinancialAssessmentDetails FinancialAssessmentDetailsEntityToFinancialAssessmentDetails(final FinancialAssessmentDetailsEntity detailsEntity);

    FinancialAssessmentEntity FinancialAssessmentDtoToFinancialAssessmentEntity(final FinancialAssessmentDTO financialAssessment);

    FinancialAssessmentDetailsEntity FinancialAssessmentDetailsToFinancialAssessmentDetailsEntity(final FinancialAssessmentDetails details);

    ChildWeightingsEntity ChildWeightingsToChildWeightingsEntity(final ChildWeightings childWeightings);

    ChildWeightings ChildWeightingsEntityToChildWeightings(final ChildWeightingsEntity childWeightingsEntity);
}