package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.dces.mapper.ContributionFileMapper;
import gov.uk.courtdata.dces.response.ContributionFileErrorResponse;
import gov.uk.courtdata.dces.response.ContributionFileResponse;
import gov.uk.courtdata.model.id.ContributionFileErrorsId;
import gov.uk.courtdata.repository.ContributionFileErrorsRepository;
import gov.uk.courtdata.repository.ContributionFilesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContributionFileService {
    private final ContributionFilesRepository contributionFileRepository;
    private final ContributionFileErrorsRepository contributionFileErrorRepository;
    private final ContributionFileMapper contributionFileMapper;

    public Optional<ContributionFileResponse> getContributionFile(int contributionFileId) {
        // TODO is it necessary to back-fill the xmlContent (problem with JPA and large text fields, but is it only on INSERT)?
        return contributionFileRepository.findById(contributionFileId).map(contributionFileMapper::toContributionFileResponse);
    }

    public List<ContributionFileErrorResponse> getAllContributionFileError(int contributionFileId) {
        return contributionFileErrorRepository.findByContributionFileId(contributionFileId).stream().map(contributionFileMapper::toContributionFileErrorResponse).toList();
    }

    public Optional<ContributionFileErrorResponse> getContributionFileError(int contributionId, int contributionFileId) {
        var primaryKey = new ContributionFileErrorsId(contributionId, contributionFileId);
        return contributionFileErrorRepository.findById(primaryKey).map(contributionFileMapper::toContributionFileErrorResponse);
    }
}
