package gov.uk.courtdata.contributions.service;

import gov.uk.courtdata.contributions.impl.ContributionsImpl;
import gov.uk.courtdata.contributions.mapper.ContributionsMapper;
import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.model.contributions.CreateContributions;
import gov.uk.courtdata.model.contributions.UpdateContributions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContributionsService {

    private ContributionsImpl contributionsImpl;
    private ContributionsMapper contributionsMapper;

    public ContributionsDTO findLatest(Integer repId) {
        ContributionsEntity contributionsEntity = contributionsImpl.findLatest(repId);
        if (contributionsEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("Latest contributions entry not found for repId %i", repId));
        }
        return contributionsMapper.mapEntityToDTO(contributionsEntity);
    }

    public ContributionsDTO update(UpdateContributions updateContributions) {
        ContributionsEntity contributionsEntity = contributionsImpl.findLatest(updateContributions.getId());
        contributionsMapper.updateContributionsToContributionsEntity(updateContributions, contributionsEntity);
        return contributionsMapper.mapEntityToDTO(contributionsImpl.update(contributionsEntity));
    }

    public ContributionsDTO create(CreateContributions createContributions) {
        ContributionsEntity contributionsEntity = contributionsMapper.createContributionsToContributionsEntity(createContributions);
        return contributionsMapper.mapEntityToDTO(contributionsImpl.create(contributionsEntity));
    }
}
