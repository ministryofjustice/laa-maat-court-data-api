package gov.uk.courtdata.contribution.service;

import gov.uk.courtdata.contribution.dto.ContributionsSummaryDTO;
import gov.uk.courtdata.contribution.mapper.ContributionsMapper;
import gov.uk.courtdata.contribution.projection.ContributionsSummaryView;
import gov.uk.courtdata.contribution.repository.ContributionsRepository;
import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.justice.laa.crime.common.model.contribution.maat_api.CreateContributionRequest;

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
            ContributionsEntity contributions = contributionsRepository.findByRepOrder_IdAndLatestIsTrue(repId);
            if (null != contributions) {
                contributionsEntityList.add(contributions);
            }
        } else {
            contributionsEntityList = contributionsRepository.findAllByRepOrder_Id(repId);
        }
        if (contributionsEntityList.isEmpty()) {
            throw new RequestedObjectNotFoundException(String.format("Contributions entry not found for repId %d", repId));
        }

        return contributionsMapper.mapEntityToDTO(contributionsEntityList);
    }

    @Transactional
    public ContributionsDTO create(CreateContributionRequest createContributions) {
        Integer repId = createContributions.getRepId();
        ContributionsEntity existingContributionsEntity = contributionsRepository.findByRepOrder_IdAndLatestIsTrue(repId);

        if (existingContributionsEntity != null) {
            contributionsRepository.updateExistingContributionToInactive(repId, createContributions.getEffectiveDate());
            contributionsRepository.updateExistingContributionToPrior(repId);
        }
        ContributionsEntity newContributionsEntity = contributionsMapper.createContributionsToContributionsEntity(createContributions);
        newContributionsEntity.setLatest(true);
        newContributionsEntity.setActive("Y");
        return contributionsMapper.mapEntityToDTO(contributionsRepository.saveAndFlush(newContributionsEntity));
    }

    public List<ContributionsSummaryDTO> getContributionsSummary(int repId) {
        List<ContributionsSummaryView> contributionsSummaryViewEntities = contributionsRepository.getContributionsSummary(repId);

        if (contributionsSummaryViewEntities.isEmpty()) {
            throw new RequestedObjectNotFoundException(String.format("No contribution entries found for repId: %d", repId));
        }
        return contributionsMapper.contributionsSummaryToContributionsSummaryDTO(contributionsSummaryViewEntities);
    }

}
