package gov.uk.courtdata.contribution.mapper;

import gov.uk.courtdata.contribution.dto.ContributionFilesDTO;
import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.contribution.model.CreateContributions;
import gov.uk.courtdata.contribution.model.UpdateContributions;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ContributionsMapper {

    ContributionsDTO contributionsEntityToContributionsDTO(ContributionsEntity contributionsEntity);

    ContributionFilesDTO contributionFilesEntityToContributionFilesDTO(ContributionFilesEntity contributionsFilesEntity);

    void updateContributionsToContributionsEntity(UpdateContributions updatecontributions, @MappingTarget ContributionsEntity contributionsEntity);

    ContributionsEntity createContributionsToContributionsEntity(CreateContributions createContributions);

    List<ContributionsDTO> contributionsEntityToContributionsDTO(List<ContributionsEntity> contributionsEntities);
}
