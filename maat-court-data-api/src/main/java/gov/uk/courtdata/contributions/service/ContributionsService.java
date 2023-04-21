package gov.uk.courtdata.contributions.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.contributions.impl.ContributionsImpl;
import gov.uk.courtdata.contributions.mapper.ContributionsMapper;
import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.model.contributions.CreateContributions;
import gov.uk.courtdata.model.contributions.UpdateContributions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@XRayEnabled
@RequiredArgsConstructor
public class ContributionsService {

    private final ContributionsImpl contributionsImpl;
    private final ContributionsMapper contributionsMapper;

    public ContributionsDTO find(int repId) {
        ContributionsEntity contributionsEntity = contributionsImpl.findLatest(repId);

        if (contributionsEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("Contributions entry not found for repId %d", repId));
        }

        return contributionsMapper.mapEntityToDTO(contributionsEntity);
    }

    @Transactional
    public ContributionsDTO update(UpdateContributions updateContributions) {
        Integer id = updateContributions.getId();
        ContributionsEntity contributionsEntity = contributionsImpl.find(id);

        if (contributionsEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("Contributions entry not found for id %d", id));
        }

        contributionsMapper.updateContributionsToContributionsEntity(updateContributions, contributionsEntity);
        return contributionsMapper.mapEntityToDTO(contributionsImpl.update(contributionsEntity));
    }

    @Transactional
    public ContributionsDTO create(CreateContributions createContributions) {
        Integer repId = createContributions.getRepId();
        ContributionsEntity existingContributionsEntity = contributionsImpl.findLatest(repId);

        if (existingContributionsEntity != null) {
            contributionsImpl.updateInactiveAndPrior(repId, createContributions.getEffectiveDate());
        }

        ContributionsEntity newContributionsEntity = contributionsMapper.createContributionsToContributionsEntity(createContributions);
        return contributionsMapper.mapEntityToDTO(contributionsImpl.create(newContributionsEntity));
    }
}
