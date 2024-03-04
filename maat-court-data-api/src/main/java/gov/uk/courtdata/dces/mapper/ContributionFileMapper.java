package gov.uk.courtdata.dces.mapper;

import gov.uk.courtdata.dces.request.CreateContributionFileRequest;
import gov.uk.courtdata.dces.request.CreateFdcFileRequest;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ContributionFileMapper {
    @Mapping(source = "xmlFileName", target = "fileName")
    ContributionFilesEntity toContributionFileEntity(CreateContributionFileRequest dto);

    @Mapping(source = "xmlFileName", target = "fileName")
    ContributionFilesEntity toContributionFileEntity(CreateFdcFileRequest dto);
}