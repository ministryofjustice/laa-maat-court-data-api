package gov.uk.courtdata.repOrder.mapper;

import gov.uk.courtdata.dto.FinAssIncomeEvidenceDTO;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.dto.RepOrderDTO;
import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.model.assessment.ChildWeightings;
import gov.uk.courtdata.model.assessment.FinancialAssessmentDetails;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true)
)
public interface RepOrderMapper {

    RepOrderDTO RepOrderEntityToRepOrderDTO(final RepOrderEntity repOrder);

    FinancialAssessmentDTO FinancialAssessmentEntityToFinancialAssessmentDTO(final FinancialAssessmentEntity financialAssessment);

    FinancialAssessmentDetailEntity FinancialAssessmentDetailsToFinancialAssessmentDetailsEntity(final FinancialAssessmentDetails details);

    FinancialAssessmentEntity FinancialAssessmentDTOToFinancialAssessmentEntity(final FinancialAssessmentDTO financialAssessment);

    FinancialAssessmentDetails FinancialAssessmentDetailsEntityToFinancialAssessmentDetails(final FinancialAssessmentDetailEntity detailsEntity);

    ChildWeightingsEntity ChildWeightingsToChildWeightingsEntity(final ChildWeightings childWeightings);

    ChildWeightings ChildWeightingsEntityToChildWeightings(final ChildWeightingsEntity childWeightingsEntity);

    FinAssIncomeEvidenceEntity finAssIncomeEvidenceDTOToFinAssIncomeEvidenceEntity(final FinAssIncomeEvidenceDTO finAssIncomeEvidenceDTO);

    FinAssIncomeEvidenceDTO finAssIncomeEvidenceEntityToFinAssIncomeEvidenceDTO(final FinAssIncomeEvidenceEntity finAssIncomeEvidenceEntity);

    }
