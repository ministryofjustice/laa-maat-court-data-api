package gov.uk.courtdata.dces.mapper;

import gov.uk.courtdata.dces.response.FdcContributionEntry;
import gov.uk.courtdata.entity.FdcContributionsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/***
 * Note this does not map the RepOrderEntity fields to the FdcContributionEntry as might be expected.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FdcContributionMapper {
    FdcContributionEntry mapFdcContribution(FdcContributionsEntity entity);
}