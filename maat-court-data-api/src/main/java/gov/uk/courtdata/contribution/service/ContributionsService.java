package gov.uk.courtdata.contribution.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.contribution.mapper.ContributionsMapper;
import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.model.contributions.CreateContributions;
import gov.uk.courtdata.model.contributions.UpdateContributions;
import gov.uk.courtdata.repository.ContributionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@XRayEnabled
@RequiredArgsConstructor
public class ContributionsService {

    private final ContributionsMapper contributionsMapper;

    private final ContributionsRepository contributionsRepository;

    public ContributionsDTO find(int repId) {
        ContributionsEntity contributionsEntity = contributionsRepository.findByRepIdAndLatestIsTrue(repId);

        if (contributionsEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("Contributions entry not found for repId %d", repId));
        }

        return contributionsMapper.mapEntityToDTO(contributionsEntity);
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
    public Integer getContributionCount(Integer repId) {
        return contributionsRepository.getContributionCount(repId);
    }
}
