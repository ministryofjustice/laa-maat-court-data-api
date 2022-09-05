package gov.uk.courtdata.assessment.mapper;

import gov.uk.courtdata.dto.FinAssIncomeEvidenceDTO;
import gov.uk.courtdata.entity.FinAssIncomeEvidenceEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface FinAssIncomeEvidenceMapper {
    List<FinAssIncomeEvidenceDTO> finAssIncomeEvidenceToFinAssIncomeEvidenceDto(List<FinAssIncomeEvidenceEntity> finAssIncomeEvidence);
}
