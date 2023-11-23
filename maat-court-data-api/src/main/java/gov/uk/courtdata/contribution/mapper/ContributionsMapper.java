package gov.uk.courtdata.contribution.mapper;

import gov.uk.courtdata.contribution.dto.ContributionsSummaryDTO;
import gov.uk.courtdata.contribution.projection.ContributionsSummaryView;
import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.contribution.model.CreateContributions;
import gov.uk.courtdata.contribution.model.UpdateContributions;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ContributionsMapper {

    @Mapping(target = "repId", source = "repOrder.id")
    ContributionsDTO mapEntityToDTO(ContributionsEntity entity);

    List<ContributionsDTO> mapEntityToDTO(List<ContributionsEntity> entity);

    void updateContributionsToContributionsEntity(UpdateContributions updatecontributions, @MappingTarget ContributionsEntity contributionsEntity);

    @Mapping(target = "repOrder.id", source = "repId")
    ContributionsEntity createContributionsToContributionsEntity(CreateContributions createContributions);

    List<ContributionsSummaryDTO> contributionsSummaryToContributionsSummaryDTO(List<ContributionsSummaryView> contributionsSummaryViews);
}
