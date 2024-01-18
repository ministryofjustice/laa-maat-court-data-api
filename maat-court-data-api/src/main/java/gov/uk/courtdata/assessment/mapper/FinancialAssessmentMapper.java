package gov.uk.courtdata.assessment.mapper;

import gov.uk.courtdata.dto.FinAssIncomeEvidenceDTO;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.dto.IOJAssessorDetails;
import gov.uk.courtdata.entity.ChildWeightingsEntity;
import gov.uk.courtdata.entity.FinAssIncomeEvidenceEntity;
import gov.uk.courtdata.entity.FinancialAssessmentDetailEntity;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.NewWorkReasonEntity;
import gov.uk.courtdata.model.NewWorkReason;
import gov.uk.courtdata.model.assessment.ChildWeightings;
import gov.uk.courtdata.model.assessment.CreateFinancialAssessment;
import gov.uk.courtdata.model.assessment.FinancialAssessmentDetails;
import gov.uk.courtdata.model.assessment.UpdateFinancialAssessment;
import gov.uk.courtdata.util.UserEntityUtils;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true)
)
public interface FinancialAssessmentMapper {

    NewWorkReason newWorkReasonEntityToNewWorkReason(final NewWorkReasonEntity newWorkReason);

    NewWorkReasonEntity newWorkReasonToNewWorkReasonEntity(final NewWorkReason newWorkReason);

    @Mapping(target = "repId", source = "repOrder.id")
    FinancialAssessmentDTO financialAssessmentEntityToFinancialAssessmentDTO(final FinancialAssessmentEntity financialAssessment);

    FinancialAssessmentDTO updateFinancialAssessmentToFinancialAssessmentDTO(final UpdateFinancialAssessment assessment);

    @Mapping(target = "newWorkReason.code", source = "nworCode")
    FinancialAssessmentDTO createFinancialAssessmentToFinancialAssessmentDTO(final CreateFinancialAssessment assessment);

    FinancialAssessmentDetails financialAssessmentDetailsEntityToFinancialAssessmentDetails(final FinancialAssessmentDetailEntity detailsEntity);

    @Mapping(target = "repOrder.id", source = "repId")
    FinancialAssessmentEntity financialAssessmentDtoToFinancialAssessmentEntity(final FinancialAssessmentDTO financialAssessment);

    FinancialAssessmentDetailEntity financialAssessmentDetailsToFinancialAssessmentDetailsEntity(final FinancialAssessmentDetails details);

    ChildWeightingsEntity childWeightingsToChildWeightingsEntity(final ChildWeightings childWeightings);

    ChildWeightings childWeightingsEntityToChildWeightings(final ChildWeightingsEntity childWeightingsEntity);

    FinAssIncomeEvidenceEntity finAssIncomeEvidenceDTOToFinAssIncomeEvidenceEntity(final FinAssIncomeEvidenceDTO finAssIncomeEvidenceDTO);

    FinAssIncomeEvidenceDTO finAssIncomeEvidenceEntityToFinAssIncomeEvidenceDTO(final FinAssIncomeEvidenceEntity finAssIncomeEvidenceEntity);

    default IOJAssessorDetails createIOJAssessorDetails(FinancialAssessmentEntity financialAssessment) {
        String fullName = UserEntityUtils.extractFullName(financialAssessment.getUserCreatedEntity());

        return IOJAssessorDetails.builder()
                .fullName(fullName)
                .userName(financialAssessment.getUserCreated())
                .build();
    }
}