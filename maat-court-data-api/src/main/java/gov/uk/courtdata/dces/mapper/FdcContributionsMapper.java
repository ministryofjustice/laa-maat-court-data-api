package gov.uk.courtdata.dces.mapper;

import gov.uk.courtdata.dces.request.FdcContributionsRequest;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FdcContributionsMapper {
    ContributionFilesEntity toFdcContributionsEntity(FdcContributionsRequest dto);
}