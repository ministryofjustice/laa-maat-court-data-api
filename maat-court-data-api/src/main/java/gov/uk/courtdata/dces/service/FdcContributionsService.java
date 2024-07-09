package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.dces.mapper.FdcContributionMapper;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static gov.uk.courtdata.enums.FdcContributionsStatus.SENT;

@Slf4j
@Service
@RequiredArgsConstructor
public class FdcContributionsService {
    private final FdcContributionsRepository fdcContributionsRepository;
    private final FdcItemsRepository fdcItemsRepository;
    private final ContributionFileMapper contributionFileMapper;
    private final DebtCollectionRepository debtCollectionRepository;
    private final DebtCollectionService debtCollectionService;
    private final FdcContributionMapper fdcContributionMapper;

    private static final Function<FdcContributionsEntity, FdcContributionEntry> BUILD_FDC_ENTRY =
            entity -> FdcContributionEntry.builder()
                    .id(entity.getId())
                    .maatId(entity.getRepOrderEntity().getId())
                    .finalCost(entity.getFinalCost())
                    .dateCalculated(entity.getDateCalculated())
                    .lgfsCost(entity.getLgfsCost())
                    .agfsCost(entity.getAgfsCost())
                    .sentenceOrderDate(Objects.nonNull(entity.getRepOrderEntity())?entity.getRepOrderEntity().getSentenceOrderDate():null)
                    .build();

    public FdcContributionsResponse getFdcContributionFiles(FdcContributionsStatus status) {
        log.info("Getting fdc contribution file with status with the -> {}", status);
        final List<FdcContributionsEntity> fdcFileList = fdcContributionsRepository.findByStatus(status);

        List<FdcContributionEntry> fdcContributionEntries = fdcFileList.stream()
                .map(BUILD_FDC_ENTRY)
                .toList();
        return FdcContributionsResponse.builder().fdcContributions(fdcContributionEntries).build();
    }

    public FdcContributionsGlobalUpdateResponse fdcContributionGlobalUpdate(){
        log.info("Running global update process for Final Defence Cost contributions.");
        int numberOfUpdates;
        boolean updateSuccessful;
        try{
            numberOfUpdates = executeGlobalUpdate();
            updateSuccessful=true;
        } catch(Exception e){
            log.error("FDC Global update failed with: {}", e.getMessage(), e);
            throw e;
        }
        return FdcContributionsGlobalUpdateResponse.builder().numberOfUpdates(numberOfUpdates).successful(updateSuccessful).build();
    }


    private int executeGlobalUpdate(){
        log.info("executeGlobalUpdate entered");
        String delay = fdcContributionsRepository.callGetFdcCalculationDelay();
        int update1Result = debtCollectionRepository.globalUpdatePart1(delay);
        log.info("FDC Global update Part 1 affected: {}", Optional.of(update1Result));
        int update2Result = debtCollectionRepository.globalUpdatePart2(delay);
        log.info("FDC Global update Part 2 affected: {}", Optional.of(update2Result));
        int response = update1Result+update2Result;
        log.info("executeGlobalUpdate exiting");
        return response;
    }

    @Transactional(rollbackFor = MAATCourtDataException.class)
    @NotNull
    public Integer createContributionFileAndUpdateFdcStatus(CreateFdcFileRequest fdcRequest){

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

    private boolean updateStatusForFdc(Set<Integer> fdcIds, ContributionFilesEntity contributionFilesEntity){
        List<FdcContributionsEntity> fdcEntities = fdcContributionsRepository.findByIdIn(fdcIds);
        if(!fdcEntities.isEmpty()) {
            fdcEntities.forEach(fdc -> {
                fdc.setStatus(SENT);
                fdc.setContFileId(contributionFilesEntity.getFileId());
                fdc.setUserModified("DCES");
            });
            log.info("Saving {} Fdc Contributions", Optional.of(fdcEntities.size()));
            fdcContributionsRepository.saveAll(fdcEntities);
            return true;
        }
        return false;
    }

    private ContributionFilesEntity createFdcFile(CreateFdcFileRequest fdcFileRequest){
        log.info("Updating the fdc file ref  -> {}", fdcFileRequest);
        ContributionFileUtil.assessFilename(fdcFileRequest);
        ContributionFilesEntity contributionFileEntity = contributionFileMapper.toContributionFileEntity(fdcFileRequest);
        debtCollectionRepository.saveContributionFilesEntity(contributionFileEntity);
        return contributionFileEntity;
    }

    @Transactional(rollbackFor =  MAATCourtDataException.class)
    @NotNull
    public Integer logFdcProcessed(LogFdcProcessedRequest request) {
        FdcContributionsEntity fdcEntity = fdcContributionsRepository.findById(Integer.valueOf(request.getFdcId()))
                .orElseThrow(() -> new RequestedObjectNotFoundException("fdc_contribution could not be found by id"));
        log.info("Contribution found: {}", fdcEntity.getId());
        if (!debtCollectionService.updateContributionFileReceivedCount(fdcEntity.getContFileId())) {
            throw new RequestedObjectNotFoundException("Contribution file could not be updated");
        }
        if(!StringUtils.isEmpty(request.getErrorText()) ){
            saveErrorMessage(request, fdcEntity);
        }
        return fdcEntity.getContFileId();
    }

    private boolean saveErrorMessage(LogFdcProcessedRequest request, FdcContributionsEntity fdcEntity){
        return debtCollectionService.saveError(ContributionFileUtil.buildContributionFileError(request, fdcEntity));
    }

    public FdcItemsEntity createFdcItems(CreateFdcItemRequest fdcRequest) {

        try {
            log.info("Create createFdcItems {}", fdcRequest);

            FdcItemsEntity fdcItemsEntity = FdcItemsEntity.builder()
                .fdcId(fdcRequest.getFdcId())
                .latestCostInd(fdcRequest.getLatestCostInd())
                .itemType(fdcRequest.getItemType())
                .adjustmentReason(fdcRequest.getAdjustmentReason())
                .userCreated(fdcRequest.getUserCreated())
                .paidAsClaimed(fdcRequest.getPaidAsClaimed())
                .dateCreated(fdcRequest.getDateCreated().toLocalDate())
                .build();

            return fdcItemsRepository.save(fdcItemsEntity);

        } catch(Exception e){
            log.error("Failed to persist data for FdcItemsEntity {}", e.getMessage());
            throw e;
        }
    }

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
                return fdcContributionsRepository.updateStatusByRepId(request.getRepId(), request.getNewStatus());
            } else {
                log.info("Updating status to {} from {} for rep order id {}", request.getNewStatus(), request.getPreviousStatus(), request.getRepId());
                return fdcContributionsRepository.updateStatus(request.getRepId(), request.getNewStatus(), request.getPreviousStatus());
            }

        } catch (Exception exception) {
            log.error("Failed to update data for FdcContributionsEntity {}", exception.getMessage());
            throw new MAATCourtDataException (exception.getMessage());
        }
    }

    public FdcContributionEntry getFdcContribution(Integer fdcContributionId){
        FdcContributionsEntity fdcEntity = fdcContributionsRepository.findById(fdcContributionId)
                .orElseThrow(() -> new RequestedObjectNotFoundException("fdc_contribution could not be found by id"));
        log.info("FDC Contribution found: {}", fdcEntity.getId());
        return fdcContributionMapper.mapFdcContribution(fdcEntity);
    }
}