package gov.uk.courtdata.contribution.mapper;

import gov.uk.courtdata.dces.response.FdcContributionEntry;
import gov.uk.courtdata.entity.FdcContributionsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FdcContributionMapper {
    FdcContributionEntry toDto(FdcContributionsEntity entity);
}