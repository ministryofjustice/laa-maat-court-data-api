package gov.uk.courtdata.repOrder.mapper;

import gov.uk.courtdata.dto.FinAssIncomeEvidenceDTO;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.dto.PassportAssessmentDTO;
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

    @Mapping(target = "repId", source = "repOrder.id")
    FinancialAssessmentDTO FinancialAssessmentEntityToFinancialAssessmentDTO(final FinancialAssessmentEntity financialAssessment);

    FinancialAssessmentDetails FinancialAssessmentDetailsEntityToFinancialAssessmentDetails(final FinancialAssessmentDetailEntity detailsEntity);

    @Mapping(target = "repId", source = "repOrder.id")
    PassportAssessmentDTO PassportAssessmentEntityToPassportAssessmentDTO(final PassportAssessmentEntity passportAssessment);

    ChildWeightings ChildWeightingsEntityToChildWeightings(final ChildWeightingsEntity childWeightingsEntity);

    FinAssIncomeEvidenceEntity finAssIncomeEvidenceDTOToFinAssIncomeEvidenceEntity(final FinAssIncomeEvidenceDTO finAssIncomeEvidenceDTO);

    FinAssIncomeEvidenceDTO finAssIncomeEvidenceEntityToFinAssIncomeEvidenceDTO(final FinAssIncomeEvidenceEntity finAssIncomeEvidenceEntity);

    }
