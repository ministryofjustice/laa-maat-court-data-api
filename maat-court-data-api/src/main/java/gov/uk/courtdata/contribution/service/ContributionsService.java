package gov.uk.courtdata.contribution.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.contribution.mapper.ContributionsMapper;
import gov.uk.courtdata.contribution.model.CreateContributions;
import gov.uk.courtdata.contribution.model.UpdateContributions;
import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.repository.ContributionsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@XRayEnabled
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

        return contributionsMapper.contributionsEntityToContributionsDTO(contributionsEntityList);
    }

    @Transactional
    public ContributionsDTO update(UpdateContributions updateContributions) {
        Integer id = updateContributions.getId();
        ContributionsEntity contributionsEntity = contributionsRepository.findById(id).orElse(null);

        if (contributionsEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("Contributions entry not found for id %d", id));
        }

        contributionsMapper.updateContributionsToContributionsEntity(updateContributions, contributionsEntity);
        return contributionsMapper.contributionsEntityToContributionsDTO(contributionsRepository.saveAndFlush(contributionsEntity));
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
        return contributionsMapper.contributionsEntityToContributionsDTO(contributionsRepository.saveAndFlush(newContributionsEntity));
    }

    @Transactional
    public int getContributionCount(Integer repId) {
        return contributionsRepository.getContributionCount(repId);
    }
}
