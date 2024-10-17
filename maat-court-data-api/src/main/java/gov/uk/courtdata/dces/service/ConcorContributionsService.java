package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.dces.mapper.ConcorContributionMapper;
import gov.uk.courtdata.dces.mapper.ContributionFileMapper;
import gov.uk.courtdata.dces.request.CreateContributionFileRequest;
import gov.uk.courtdata.dces.request.LogContributionProcessedRequest;
import gov.uk.courtdata.dces.request.UpdateConcorContributionStatusRequest;
import gov.uk.courtdata.dces.response.ConcorContributionResponse;
import gov.uk.courtdata.dces.response.ConcorContributionResponseDTO;
import gov.uk.courtdata.dces.util.ContributionFileUtil;
import gov.uk.courtdata.entity.ConcorContributionsEntity;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.enums.ConcorContributionStatus;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.repository.ConcorContributionsRepository;
import gov.uk.courtdata.util.ValidationUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static gov.uk.courtdata.enums.ConcorContributionStatus.SENT;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConcorContributionsService {
    private static final Integer DEFAULT_RECORD_COUNT = 500;
    private static final String USER_AUDIT = "DCES";
    private final ConcorContributionsRepository concorRepository;
    private final ContributionFileMapper contributionFileMapper;
    private final DebtCollectionRepository debtCollectionRepository;
    private final DebtCollectionService debtCollectionService;
    private final ConcorContributionMapper concorContributionMapper;

    /** Resets a number of concor_contribution rows to status = ACTIVE | REPLACED, contrib_file_id = (null). */
    @Transactional
    public List<Integer> updateConcorContributionStatusAndResetContribFile(UpdateConcorContributionStatusRequest request) {
        List<Integer> idsToUpdate = concorRepository.findIdsForUpdate(Pageable.ofSize(request.getRecordCount()));
        if (!idsToUpdate.isEmpty()) {
            concorRepository.updateStatusAndResetContribFileForIds(request.getStatus(), USER_AUDIT, idsToUpdate);
        }
        return idsToUpdate;
    }

    public List<ConcorContributionResponse> getConcorContributionFiles(ConcorContributionStatus status, Integer noOfRecords, Integer concorContributionId) {

        concorContributionId = Optional.ofNullable(concorContributionId).orElse(0);
        noOfRecords = Optional.ofNullable(noOfRecords).orElse(DEFAULT_RECORD_COUNT);

        log.info("Searching concor contribution file with status {}, startId {} and count {}", status, concorContributionId, noOfRecords);
        Pageable pageable = PageRequest.of(0, noOfRecords, Sort.by("id"));
        final List<ConcorContributionsEntity> concorFileList = concorRepository.findByStatusAndIdGreaterThan(status, concorContributionId, pageable);

        return concorFileList.stream().map(cc -> ConcorContributionResponse.builder()
                        .concorContributionId(cc.getId())
                        .xmlContent(cc.getCurrentXml())
                        .build()).toList();
    }

    @Transactional(rollbackFor = MAATCourtDataException.class)
    @NotNull
    public Integer createContributionAndUpdateConcorStatus(CreateContributionFileRequest contributionRequest){
        ValidationUtils.isNull(contributionRequest,"contributionRequest object is null");
        ValidationUtils.isEmptyOrHasNullElement(contributionRequest.getConcorContributionIds(),"ContributionIds are empty/null.");
        final ContributionFilesEntity contributionFilesEntity = createContributionFile(contributionRequest);
        if (!updateConcorContributionStatusAndSetContribFile(contributionRequest.getConcorContributionIds(), SENT, contributionFilesEntity.getFileId())) {
            throw new NoSuchElementException("No concor_contribution status values were updated"); // did not rollback previously
        }
        if (contributionFilesEntity.getFileId() == null) {
            throw new RequestedObjectNotFoundException("Created contribution_file's id could not be found"); // did not rollback previously
        }
        return contributionFilesEntity.getFileId();
    }

    @Transactional(rollbackFor =  MAATCourtDataException.class)
    @NotNull
    public Integer logContributionProcessed(LogContributionProcessedRequest request) {
        ConcorContributionsEntity concorEntity = concorRepository.findById(request.getConcorId())
                .orElseThrow(() -> new RequestedObjectNotFoundException("log concor_contribution ID " + request.getConcorId() + ": not found"));
        log.info("log concor_contribution ID {}: found OK", concorEntity.getId());
        if (!StringUtils.isEmpty(request.getErrorText())) {
            saveErrorMessage(request, concorEntity);
        } else if (!debtCollectionService.updateContributionFileReceivedCount(concorEntity.getContribFileId())) {
            throw new NoSuchElementException("log contribution_file ID " + concorEntity.getContribFileId()
                    + " (associated with concur_contribution ID " + request.getConcorId() + "): not found");
        }
        return concorEntity.getContribFileId();
    }

    public ConcorContributionResponseDTO getConcorContribution(Integer concorContributionId) {
        Optional<ConcorContributionsEntity> concorContributionEntity = concorRepository.findById(concorContributionId);
        if (concorContributionEntity.isPresent()) {
            log.info("Concor Contribution found: {}", concorContributionEntity.get().getId());
            return concorContributionMapper.toConcorContributionResponseDTO(concorContributionEntity.get());
        } else {
            log.info("Concor Contribution not found: {}", concorContributionId);
        }
        return null;
    }

    private void saveErrorMessage(LogContributionProcessedRequest request, ConcorContributionsEntity concorEntity) {
        debtCollectionService.saveError(ContributionFileUtil.buildContributionFileError(request, concorEntity));
    }

    private ContributionFilesEntity createContributionFile(CreateContributionFileRequest contributionRequest) {
        final ContributionFilesEntity contributionFileEntity;
        try {
            log.info("Updating the concor contribution file ref  -> {}", contributionRequest);
            ContributionFileUtil.assessFilename(contributionRequest);
            contributionFileEntity = contributionFileMapper.toContributionFileEntity(contributionRequest);
            debtCollectionRepository.saveContributionFilesEntity(contributionFileEntity);
        } catch (Exception e) {
            throw new MAATCourtDataException("Failed to map ConcorContributionRequest to ContributionFilesEntity and persist: [%s]".formatted(e.getMessage()));
        }
        return contributionFileEntity;
    }

    /** Sets specific concor_contribution rows to status = SENT, contrib_file_id = (non-null). */
    private boolean updateConcorContributionStatusAndSetContribFile(Set<Integer> ids, ConcorContributionStatus status, final Integer contributionFileId) {
        final List<ConcorContributionsEntity> concorFileList = concorRepository.findByIdIn(ids);
        log.info("Concor Contributions for status update - count {}", concorFileList.size());
        concorFileList.forEach(cc -> {
            cc.setUserModified(USER_AUDIT);
            cc.setDateModified(LocalDate.now());
            cc.setStatus(status);
            cc.setContribFileId(contributionFileId);
        });
        if (!concorFileList.isEmpty()) {
            concorRepository.saveAll(concorFileList);
            log.info("Updated all concor contribution file for contributionFileId {} and count {}",
                    contributionFileId, concorFileList.size());
            return true;
        } else {
            log.info("Concor Contributions files empty {}", ids);
            return false;
        }
    }
}
