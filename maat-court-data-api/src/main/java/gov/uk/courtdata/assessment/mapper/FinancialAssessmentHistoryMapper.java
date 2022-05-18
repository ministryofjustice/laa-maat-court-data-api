package gov.uk.courtdata.assessment.mapper;

import gov.uk.courtdata.dto.ChildWeightHistoryDTO;
import gov.uk.courtdata.dto.FinancialAssessmentDetailsHistoryDTO;
import gov.uk.courtdata.dto.FinancialAssessmentsHistoryDTO;
import gov.uk.courtdata.entity.*;
import org.mapstruct.*;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {FinancialAssessmentMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true)
)
public interface FinancialAssessmentHistoryMapper {
    @Mapping(source = "id", target = "fasdId")
    FinancialAssessmentDetailsHistoryDTO FinancialAssessmentDetailEntityToFinancialAssessmentDetailsHistoryDTO(final FinancialAssessmentDetailEntity financialAssessmentDetailEntity);

    FinancialAssessmentDetailsHistoryEntity FinancialAssessmentDetailsHistoryDTOToFinancialAssessmentDetailsHistoryEntity(final FinancialAssessmentDetailsHistoryDTO financialAssessmentDetailsHistoryDTO);

    FinancialAssessmentsHistoryDTO FinancialAssessmentEntityToFinancialAssessmentsHistoryDTO(final FinancialAssessmentEntity assessmentEntity);

    FinancialAssessmentsHistoryEntity FinancialAssessmentsHistoryDTOToFinancialAssessmentsHistoryEntity(final FinancialAssessmentsHistoryDTO assessmentsHistoryDTO);

    ChildWeightHistoryDTO ChildWeightingsEntityToChildWeightHistoryDTO(final ChildWeightingsEntity childWeightingsEntity);

    ChildWeightHistoryEntity ChildWeightHistoryDTOToChildWeightHistoryEntity(final ChildWeightHistoryDTO childWeightHistoryDTO);
}
