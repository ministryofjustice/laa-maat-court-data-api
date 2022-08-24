package gov.uk.courtdata.assessment.mapper;

import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.entity.ChildWeightingsEntity;
import gov.uk.courtdata.entity.FinancialAssessmentDetailEntity;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.NewWorkReasonEntity;
import gov.uk.courtdata.model.NewWorkReason;
import gov.uk.courtdata.model.assessment.ChildWeightings;
import gov.uk.courtdata.model.assessment.CreateFinancialAssessment;
import gov.uk.courtdata.model.assessment.FinancialAssessmentDetails;
import gov.uk.courtdata.model.assessment.UpdateFinancialAssessment;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true)
)
public interface FinancialAssessmentMapper {

    NewWorkReason NewWorkReasonEntityToNewWorkReason(final NewWorkReasonEntity newWorkReason);

    NewWorkReasonEntity NewWorkReasonToNewWorkReasonEntity(final NewWorkReason newWorkReason);

    @Mapping(target = "repId", source = "repOrder.id")
    FinancialAssessmentDTO FinancialAssessmentEntityToFinancialAssessmentDTO(final FinancialAssessmentEntity financialAssessment);

    FinancialAssessmentDTO UpdateFinancialAssessmentToFinancialAssessmentDTO(final UpdateFinancialAssessment assessment);

    @Mapping(target = "newWorkReason.code", source = "nworCode")
    FinancialAssessmentDTO CreateFinancialAssessmentToFinancialAssessmentDTO(final CreateFinancialAssessment assessment);

    FinancialAssessmentDetails FinancialAssessmentDetailsEntityToFinancialAssessmentDetails(final FinancialAssessmentDetailEntity detailsEntity);

    @Mapping(target = "repOrder.id", source = "repId")
    FinancialAssessmentEntity FinancialAssessmentDtoToFinancialAssessmentEntity(final FinancialAssessmentDTO financialAssessment);

    FinancialAssessmentDetailEntity FinancialAssessmentDetailsToFinancialAssessmentDetailsEntity(final FinancialAssessmentDetails details);

    ChildWeightingsEntity ChildWeightingsToChildWeightingsEntity(final ChildWeightings childWeightings);

    ChildWeightings ChildWeightingsEntityToChildWeightings(final ChildWeightingsEntity childWeightingsEntity);

}