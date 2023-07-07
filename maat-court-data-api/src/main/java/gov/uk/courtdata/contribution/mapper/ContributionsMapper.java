package gov.uk.courtdata.contribution.mapper;

import gov.uk.courtdata.contribution.dto.ContributionSummaryDTO;
import gov.uk.courtdata.contribution.projection.ContributionsSummary;
import gov.uk.courtdata.dto.ContributionsDTO;
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

    ContributionsDTO mapEntityToDTO(ContributionsEntity entity);

    void updateContributionsToContributionsEntity(UpdateContributions updatecontributions, @MappingTarget ContributionsEntity contributionsEntity);

    ContributionsEntity createContributionsToContributionsEntity(CreateContributions createContributions);

    List<ContributionsDTO> mapEntityToDTO(List<ContributionsEntity> entity);

    List<ContributionSummaryDTO> mapProjectionToDTO(List<ContributionsSummary> projections);
}
