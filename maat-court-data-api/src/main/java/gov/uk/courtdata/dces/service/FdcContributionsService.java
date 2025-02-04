package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.dces.mapper.ContributionFileMapper;
import gov.uk.courtdata.dces.request.CreateFdcContributionRequest;
import gov.uk.courtdata.dces.request.CreateFdcFileRequest;
import gov.uk.courtdata.dces.request.CreateFdcItemRequest;
import gov.uk.courtdata.dces.request.LogFdcProcessedRequest;
import gov.uk.courtdata.dces.request.UpdateFdcContributionRequest;
import gov.uk.courtdata.dces.response.FdcContributionEntry;
import gov.uk.courtdata.dces.response.FdcContributionsGlobalUpdateResponse;
import gov.uk.courtdata.dces.response.FdcContributionsResponse;
import gov.uk.courtdata.dces.util.ContributionFileUtil;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.entity.FdcContributionsEntity;
import gov.uk.courtdata.entity.FdcItemsEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.repository.FdcContributionsRepository;
import gov.uk.courtdata.repository.FdcItemsRepository;
import gov.uk.courtdata.util.ValidationUtils;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static gov.uk.courtdata.enums.FdcContributionsStatus.SENT;

@Slf4j
@Service
@RequiredArgsConstructor
public class FdcContributionsService {
    private static final String USER_AUDIT = "DCES";
    private final FdcContributionsRepository fdcContributionsRepository;
    private final FdcItemsRepository fdcItemsRepository;
    private final ContributionFileMapper contributionFileMapper;
    private final DebtCollectionRepository debtCollectionRepository;
    private final DebtCollectionService debtCollectionService;

    private FdcContributionsResponse buildFdcContributionsResponse(Supplier<List<FdcContributionsEntity>> repositoryCall) {
        return FdcContributionsResponse.builder()
            .fdcContributions(repositoryCall.get().stream()
                .map(this::mapContributionEntry)
            .toList())
            .build();
    }

    /***
     * Maps the Entity into a Entry to be sent in the response, trimming un-needed fields.
     * Note that FdcContributionMapper.mapContributionEntry does not map the RepOrderEntity fields.
     * @param entity FdcContributionEntity Entity to be mapped
     * @return mapped entity to be sent in response.
     */
    private FdcContributionEntry mapContributionEntry(FdcContributionsEntity entity){
        return FdcContributionEntry.builder()
                .id(entity.getId())
                .finalCost(entity.getFinalCost())
                .dateCalculated(entity.getDateCalculated())
                .lgfsCost(entity.getLgfsCost())
                .agfsCost(entity.getAgfsCost())
                .maatId(entity.getRepOrderEntity().getId())
                .sentenceOrderDate(Objects.nonNull(entity.getRepOrderEntity()) ? entity.getRepOrderEntity().getSentenceOrderDate() : null)
                .build();
    }

    public FdcContributionsResponse getFdcContributions(FdcContributionsStatus status) {
        log.info("Getting fdc contributions with status -> {}", status);
        return buildFdcContributionsResponse(() -> debtCollectionRepository.getFdcEntriesByStatus(status));
    }

    public FdcContributionsResponse getFdcContributions(List<Integer> fdcContributionIdList) {
        log.info("Getting fdc contributions for IDs in a list of size {}", fdcContributionIdList.size());
        return buildFdcContributionsResponse(() -> debtCollectionRepository.getFdcEntriesByIdIn(new HashSet<>(fdcContributionIdList)));
    }

    public FdcContributionsGlobalUpdateResponse fdcContributionGlobalUpdate() {
        log.info("Running global update process for Final Defence Cost contributions.");
        int numberOfUpdates;
        boolean updateSuccessful;
        try {
            numberOfUpdates = executeGlobalUpdate();
            updateSuccessful = true;
        } catch (Exception e) {
            log.error("FDC Global update failed with: {}", e.getMessage(), e);
            throw e;
        }
        return FdcContributionsGlobalUpdateResponse.builder().numberOfUpdates(numberOfUpdates).successful(updateSuccessful).build();
    }

    private int executeGlobalUpdate() {
        log.info("executeGlobalUpdate entered");
        String delay = fdcContributionsRepository.callGetFdcCalculationDelay();
        int fdcDelayedPickupResult = debtCollectionRepository.setEligibleForFdcDelayedPickup(delay);
        log.info("FDC Global update: eligibleForFdcDelayedPickup: affected: {}", Optional.of(fdcDelayedPickupResult));
        int fdcFastTrackingResult = debtCollectionRepository.setEligibleForFdcFastTracking(delay);
        log.info("FDC Global update: eligibleForFdcFastTracking: affected: {}", Optional.of(fdcFastTrackingResult));
        int response = fdcDelayedPickupResult+fdcFastTrackingResult;
        log.info("executeGlobalUpdate exiting");
        return response;
    }

    @Transactional(rollbackFor = MAATCourtDataException.class)
    @NotNull
    public Integer createContributionFileAndUpdateFdcStatus(CreateFdcFileRequest fdcRequest) {
        ValidationUtils.isNull(fdcRequest,"fdcRequest object is null");
        ValidationUtils.isEmptyOrHasNullElement(fdcRequest.getFdcIds(),"FdcIds is empty/null.");
        log.info("Request Validated");
        ContributionFilesEntity contributionFilesEntity = createFdcFile(fdcRequest);
        log.info("File created with id: {}", contributionFilesEntity.getFileId());
        if (!updateStatusForFdc(fdcRequest.getFdcIds(), contributionFilesEntity)) {
            throw new NoSuchElementException("No fdc_contribution status values were updated"); // did not rollback previously
        }
        if (contributionFilesEntity.getFileId() == null) {
            throw new RequestedObjectNotFoundException("Created contribution_file's id could not be found"); // did not rollback previously
        }
        return contributionFilesEntity.getFileId();
    }

    private boolean updateStatusForFdc(Set<Integer> fdcIds, ContributionFilesEntity contributionFilesEntity) {
        List<FdcContributionsEntity> fdcEntities = fdcContributionsRepository.findByIdIn(fdcIds);
        if (!fdcEntities.isEmpty()) {
            fdcEntities.forEach(fdc -> {
                fdc.setStatus(SENT);
                fdc.setContFileId(contributionFilesEntity.getFileId());
                fdc.setUserModified(USER_AUDIT);
                fdc.setDateModified(LocalDate.now());
            });
            log.info("Saving {} Fdc Contributions", Optional.of(fdcEntities.size()));
            fdcContributionsRepository.saveAll(fdcEntities);
            return true;
        }
        return false;
    }

    private ContributionFilesEntity createFdcFile(CreateFdcFileRequest fdcFileRequest) {
        log.info("Updating the fdc file ref  -> {}", fdcFileRequest);
        ContributionFileUtil.assessFilename(fdcFileRequest);
        ContributionFilesEntity contributionFileEntity = contributionFileMapper.toContributionFileEntity(fdcFileRequest);
        debtCollectionRepository.saveContributionFilesEntity(contributionFileEntity);
        return contributionFileEntity;
    }

    @Transactional(rollbackFor =  MAATCourtDataException.class)
    @NotNull
    public Integer logFdcProcessed(LogFdcProcessedRequest request) {
        FdcContributionsEntity fdcEntity = fdcContributionsRepository.findById(request.getFdcId())
                .orElseThrow(() -> new RequestedObjectNotFoundException("log fdc_contribution ID " + request.getFdcId() + ": not found"));
        log.info("log fdc_contribution ID {}: found OK", fdcEntity.getId());
        if (!StringUtils.isEmpty(request.getErrorText())) {
            if (fdcEntity.getContFileId() == null) {
                // returns HTTP status 400 with error code "Object Not Found" (rather than "DB error" as it would without this check).
                throw new NoSuchElementException("log contribution_file (associated with fd_contribution ID "
                        + request.getFdcId() + "): not found");
            }
            saveErrorMessage(request, fdcEntity);
        } else if (!debtCollectionService.updateContributionFileReceivedCount(fdcEntity.getContFileId())) {
            throw new NoSuchElementException("log contribution_file ID " + fdcEntity.getContFileId()
                    + " (associated with fdc_contribution ID " + request.getFdcId() + "): not found");
        }
        return fdcEntity.getContFileId();
    }

    private void saveErrorMessage(LogFdcProcessedRequest request, FdcContributionsEntity fdcEntity) {
        debtCollectionService.saveError(ContributionFileUtil.buildContributionFileError(request, fdcEntity));
    }

    public FdcItemsEntity createFdcItems(CreateFdcItemRequest fdcRequest) {
        // set some default values for dces.
        if(Objects.isNull(fdcRequest.getDateCreated())){
            fdcRequest.setDateCreated(LocalDate.now());
        }
        if(Objects.isNull(fdcRequest.getUserCreated())){
            fdcRequest.setUserCreated(USER_AUDIT);
        }

        try {
            log.info("Create createFdcItems {}", fdcRequest);
            FdcItemsEntity fdcItemsEntity = FdcItemsEntity.builder()
                .fdcId(fdcRequest.getFdcId())
                .latestCostInd(fdcRequest.getLatestCostInd())
                .itemType(fdcRequest.getItemType())
                .adjustmentReason(fdcRequest.getAdjustmentReason())
                .userCreated(fdcRequest.getUserCreated())
                .paidAsClaimed(fdcRequest.getPaidAsClaimed())
                .dateCreated(fdcRequest.getDateCreated())
                .dateModified(fdcRequest.getDateCreated())
                .userModified(fdcRequest.getUserCreated())
                .build();
            return fdcItemsRepository.save(fdcItemsEntity);
        } catch (Exception e) {
            log.error("Failed to persist data for FdcItemsEntity {}", e.getMessage());
            throw e;
        }
    }

    @Transactional
    public long deleteFdcItems(Integer fdcId) {
        log.info("Delete FdcItemsEntity with fdcId {}", fdcId);
        try {
            return fdcItemsRepository.deleteByFdcId(fdcId);
        } catch (EmptyResultDataAccessException resultDataAccessException) {
            log.error("No FdcItemsEntity found with id {}", fdcId, resultDataAccessException);
            throw resultDataAccessException;
        } catch (DataAccessException dataAccessException) {
            log.error("Failed to delete FdcItemsEntity with id {}", fdcId, dataAccessException);
            throw dataAccessException;
        }
    }

    public FdcContributionsEntity createFdcContribution(CreateFdcContributionRequest request) {
        try {
            log.info("Create FdcContributionRequest {}", request);
            FdcContributionsEntity fdcContributionsEntity = FdcContributionsEntity.builder()
                    .repOrderEntity(RepOrderEntity.builder().id(request.getRepId()).build())
                    .lgfsComplete(request.getLgfsComplete())
                    .agfsComplete(request.getAgfsComplete())
                    .accelerate(request.getManualAcceleration())
                    .status(request.getStatus())
                    .dateCreated(LocalDate.now())
                    .dateModified(LocalDate.now())
                    .userCreated(USER_AUDIT)
                    .userModified(USER_AUDIT)
                    .build();
            return fdcContributionsRepository.save(fdcContributionsEntity);
        } catch (Exception exception) {
            log.error("Failed to persist data for FdcContributionsEntity {}", exception.getMessage());
            throw exception;
        }
    }

    @Transactional
    public Integer updateFdcContribution(UpdateFdcContributionRequest request) {
        try {
            if (request.getPreviousStatus() == null) {
                log.info("Updating status to {} for all based on rep order id {}", request.getNewStatus(), request.getRepId());
                return fdcContributionsRepository.updateStatusByRepId(request.getRepId(), USER_AUDIT, request.getNewStatus());
            } else {
                log.info("Updating status to {} from {} for rep order id {}", request.getNewStatus(), request.getPreviousStatus(), request.getRepId());
                return fdcContributionsRepository.updateStatus(request.getRepId(), USER_AUDIT, request.getNewStatus(), request.getPreviousStatus());
            }
        } catch (Exception exception) {
            log.error("Failed to update data for FdcContributionsEntity {}", exception.getMessage());
            throw new MAATCourtDataException (exception.getMessage());
        }
    }

    public FdcContributionEntry getFdcContribution(Integer fdcContributionId) {
        FdcContributionsEntity fdcEntity = fdcContributionsRepository.findById(fdcContributionId)
                .orElseThrow(() -> new RequestedObjectNotFoundException("fdc_contribution could not be found by id"));
        log.info("FDC Contribution found: {}", fdcEntity.getId());
        return mapContributionEntry(fdcEntity);
    }

}
