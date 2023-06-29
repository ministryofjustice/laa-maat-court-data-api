package gov.uk.courtdata.contribution.mapper;

import gov.uk.courtdata.contribution.dto.ContributionFilesDTO;
import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.contribution.model.CreateContributions;
import gov.uk.courtdata.contribution.model.UpdateContributions;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ContributionsMapper {

    @Mapping(target = "contributionFileId", source = "contributionFile.id")
    ContributionsDTO contributionsEntityToContributionsDTO(ContributionsEntity contributionsEntity);

    List<ContributionsDTO> contributionsEntityToContributionsDTO(List<ContributionsEntity> contributionsEntities);

    ContributionFilesDTO contributionFilesEntityToContributionFilesDTO(ContributionFilesEntity contributionsFilesEntity);

    @Mapping(target = "contributionFile.id", source = "contributionFileId")
    void updateContributionsToContributionsEntity(UpdateContributions updatecontributions, @MappingTarget ContributionsEntity contributionsEntity);

    @Mapping(target = "contributionFile.id", source = "contributionFileId")
    ContributionsEntity createContributionsToContributionsEntity(CreateContributions createContributions);
}
