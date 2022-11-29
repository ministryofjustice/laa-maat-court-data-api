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
    FinancialAssessmentDetailsHistoryDTO financialAssessmentDetailEntityToFinancialAssessmentDetailsHistoryDTO(final FinancialAssessmentDetailEntity financialAssessmentDetailEntity);

    FinancialAssessmentDetailsHistoryEntity financialAssessmentDetailsHistoryDTOToFinancialAssessmentDetailsHistoryEntity(final FinancialAssessmentDetailsHistoryDTO financialAssessmentDetailsHistoryDTO);

    @Mapping(target = "repId", source = "repOrder.id")
    FinancialAssessmentsHistoryDTO financialAssessmentEntityToFinancialAssessmentsHistoryDTO(final FinancialAssessmentEntity assessmentEntity);

    FinancialAssessmentsHistoryEntity financialAssessmentsHistoryDTOToFinancialAssessmentsHistoryEntity(final FinancialAssessmentsHistoryDTO assessmentsHistoryDTO);

    @Mapping(source = "id", target = "facwId")
    ChildWeightHistoryDTO childWeightingsEntityToChildWeightHistoryDTO(final ChildWeightingsEntity childWeightingsEntity);

    ChildWeightHistoryEntity childWeightHistoryDTOToChildWeightHistoryEntity(final ChildWeightHistoryDTO childWeightHistoryDTO);

    default String booleanToString(final Boolean bool) {
        return (bool == null || !bool) ? "N" : "Y";
    }
}
