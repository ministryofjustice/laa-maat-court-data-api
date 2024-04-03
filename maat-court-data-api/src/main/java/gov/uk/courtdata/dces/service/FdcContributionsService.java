package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.dces.mapper.ContributionFileMapper;
import gov.uk.courtdata.dces.request.CreateFdcFileRequest;
import gov.uk.courtdata.dces.request.LogFdcProcessedRequest;
import gov.uk.courtdata.dces.response.FdcContributionEntry;
import gov.uk.courtdata.dces.response.FdcContributionsGlobalUpdateResponse;
import gov.uk.courtdata.dces.response.FdcContributionsResponse;
import gov.uk.courtdata.dces.util.ContributionFileUtil;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.entity.FdcContributionsEntity;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.repository.FdcContributionsRepository;
import gov.uk.courtdata.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    private final ContributionFileMapper contributionFileMapper;
    private final DebtCollectionRepository debtCollectionRepository;
    private final DebtCollectionService debtCollectionService;

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
        int[] update1Result = debtCollectionRepository.globalUpdatePart1();
        log.info("FDC Global update Part 1 affected: {}", getResult(update1Result));
        int[] update2Result = debtCollectionRepository.globalUpdatePart2();
        log.info("FDC Global update Part 2 affected: {}", getResult(update2Result));
        int response = combineGlobalUpdateResults(update1Result, update2Result);
        log.info("executeGlobalUpdate exiting");
        return response;
    }

    @Transactional(rollbackFor = MAATCourtDataException.class)
    public boolean createContributionFileAndUpdateFdcStatus(CreateFdcFileRequest fdcRequest){

        ValidationUtils.isNull(fdcRequest,"fdcRequest object is null");
        ValidationUtils.isEmptyOrHasNullElement(fdcRequest.getFdcIds(),"FdcIds is empty/null.");
        log.info("Request Validated");
        ContributionFilesEntity contributionFilesEntity = createFdcFile(fdcRequest);
        log.info("File created with id: {}", contributionFilesEntity.getId());

        return updateStatusForFdc(fdcRequest.getFdcIds(), contributionFilesEntity);
    }

    private boolean updateStatusForFdc(Set<Integer> fdcIds, ContributionFilesEntity contributionFilesEntity){
        List<FdcContributionsEntity> fdcEntities = fdcContributionsRepository.findByIdIn(fdcIds);
        if(!fdcEntities.isEmpty()) {
            fdcEntities.forEach(fdc -> {
                fdc.setStatus(SENT);
                fdc.setContFileId(contributionFilesEntity.getId());
                fdc.setUserModified("DCES");
            });
            log.info("Saving {} Fdc Contributions", fdcEntities.size());
            fdcContributionsRepository.saveAll(fdcEntities);
            return true;
        }
        return false;
    }

    private ContributionFilesEntity createFdcFile(CreateFdcFileRequest fdcFileRequest){
        log.info("Updating the fdc file ref  -> {}", fdcFileRequest);
        ContributionFileUtil.assessFilename(fdcFileRequest);
        ContributionFilesEntity contributionFileEntity = contributionFileMapper.toContributionFileEntity(fdcFileRequest);
        debtCollectionRepository.save(contributionFileEntity);
        return contributionFileEntity;
    }

    private int combineGlobalUpdateResults(int[] part1Results, int[] part2Results){
        return getResult(part1Results)+getResult(part2Results);
    }
    private int getResult(int[] results){
        return ArrayUtils.isNotEmpty(results) ? results[0] : 0;
    }

    public boolean logFdcProcessed(LogFdcProcessedRequest request) {
        boolean successful = false;
        Optional<FdcContributionsEntity> optionalFdcEntry = fdcContributionsRepository.findById(request.getFdcId());
        if(optionalFdcEntry.isPresent()) {
            FdcContributionsEntity fdcEntity = optionalFdcEntry.get();
            log.info("Contribution found: {}", fdcEntity.getId());
            successful = debtCollectionService.updateContributionFileReceivedCount(fdcEntity.getContFileId());
            // check if error
            if(successful && !StringUtils.isEmpty(request.getErrorText()) ){
                successful = saveErrorMessage(request, fdcEntity);
            }

        }
        return successful;
    }

    private boolean saveErrorMessage(LogFdcProcessedRequest request, FdcContributionsEntity fdcEntity){
        return debtCollectionService.saveError(ContributionFileUtil.buildContributionFileError(request, fdcEntity));
    }

}