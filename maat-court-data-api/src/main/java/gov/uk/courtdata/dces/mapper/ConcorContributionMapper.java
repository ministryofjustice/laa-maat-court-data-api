package gov.uk.courtdata.dces.mapper;

import gov.uk.courtdata.dces.response.ConcorContributionResponseDTO;
import gov.uk.courtdata.entity.ConcorContributionsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ConcorContributionMapper {
    ConcorContributionResponseDTO toConcorContributionResponseDTO(ConcorContributionsEntity concorContributionsEntity);
}