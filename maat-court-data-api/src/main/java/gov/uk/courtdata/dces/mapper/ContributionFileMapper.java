package gov.uk.courtdata.dces.mapper;

import gov.uk.courtdata.dces.request.ConcorContributionRequest;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ContributionFileMapper {
    ContributionFilesEntity toContributionFileEntity(ConcorContributionRequest dto);
}