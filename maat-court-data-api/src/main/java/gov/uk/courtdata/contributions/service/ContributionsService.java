package gov.uk.courtdata.contributions.service;

import gov.uk.courtdata.contributions.impl.ContributionsImpl;
import gov.uk.courtdata.contributions.mapper.ContributionsMapper;
import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
