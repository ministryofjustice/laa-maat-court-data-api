package gov.uk.courtdata.dces.mapper;

import gov.uk.courtdata.dces.dto.ContributionFilesDTO;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true))

public interface ContributionFilesMapper {

    List<ContributionFilesDTO> contributionFilesEntityToContributionFilesDTO(List<ContributionFilesEntity> contributionFilesEntityList);
}
