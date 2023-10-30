package gov.uk.courtdata.contribution.service;

import gov.uk.courtdata.contribution.dto.ContributionsSummaryDTO;
import gov.uk.courtdata.contribution.mapper.ContributionsMapper;
import gov.uk.courtdata.contribution.model.CreateContributions;
import gov.uk.courtdata.contribution.model.UpdateContributions;
import gov.uk.courtdata.contribution.projection.ContributionsSummaryView;
import gov.uk.courtdata.contribution.repository.ContributionsRepository;
import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContributionsService {

    private final ContributionsMapper contributionsMapper;

    private final ContributionsRepository contributionsRepository;

    public List<ContributionsDTO> find(int repId, boolean findLatestContribution) {

        List<ContributionsEntity> contributionsEntityList = new ArrayList<>();
        if (findLatestContribution) {
            ContributionsEntity contributions = contributionsRepository.findByRepIdAndLatestIsTrue(repId);
            if (null != contributions) {
                contributionsEntityList.add(contributions);
            }
        } else {
            contributionsEntityList = contributionsRepository.findAllByRepId(repId);
        }
        if (contributionsEntityList.isEmpty()) {
            throw new RequestedObjectNotFoundException(String.format("Contributions entry not found for repId %d", repId));
        }

        return contributionsMapper.mapEntityToDTO(contributionsEntityList);
    }

    @Transactional
    public ContributionsDTO update(UpdateContributions updateContributions) {
        Integer id = updateContributions.getId();
        ContributionsEntity contributionsEntity = contributionsRepository.findById(id).orElse(null);

        if (contributionsEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("Contributions entry not found for id %d", id));
        }

        contributionsMapper.updateContributionsToContributionsEntity(updateContributions, contributionsEntity);
        return contributionsMapper.mapEntityToDTO(contributionsRepository.saveAndFlush(contributionsEntity));
    }

    @Transactional
    public ContributionsDTO create(CreateContributions createContributions) {
        Integer repId = createContributions.getRepId();
        ContributionsEntity existingContributionsEntity = contributionsRepository.findByRepIdAndLatestIsTrue(repId);

        if (existingContributionsEntity != null) {
            contributionsRepository.updateExistingContributionToInactive(repId, createContributions.getEffectiveDate());
            contributionsRepository.updateExistingContributionToPrior(repId);
        }
        ContributionsEntity newContributionsEntity = contributionsMapper.createContributionsToContributionsEntity(createContributions);
        newContributionsEntity.setLatest(true);
        return contributionsMapper.mapEntityToDTO(contributionsRepository.saveAndFlush(newContributionsEntity));
    }

    @Transactional
    public int getContributionCount(Integer repId) {
        return contributionsRepository.getContributionCount(repId);
    }

    public List<ContributionsSummaryDTO> getContributionsSummary(int repId) {
        List<ContributionsSummaryView> contributionsSummaryViewEntities = contributionsRepository.getContributionsSummary(repId);

        if (contributionsSummaryViewEntities.isEmpty()) {
            throw new RequestedObjectNotFoundException(String.format("No contribution entries found for repId: %d", repId));
        }
        return contributionsMapper.contributionsSummaryToContributionsSummaryDTO(contributionsSummaryViewEntities);
    }


    public ContributionsDTO findByRepIdAndLatestSentContribution(int repId) {
        ContributionsEntity contributions = contributionsRepository.findByRepIdAndLatestSentContribution(repId);
        return contributionsMapper.mapEntityToDTO(contributions);
    }

}
