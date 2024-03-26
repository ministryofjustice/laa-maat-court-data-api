package gov.uk.courtdata.dces.service;

import static gov.uk.courtdata.enums.ConcorContributionStatus.SENT;

import gov.uk.courtdata.dces.request.CreateContributionFileRequest;
import gov.uk.courtdata.dces.request.LogContributionProcessedRequest;
import gov.uk.courtdata.dces.response.ConcorContributionResponse;
import gov.uk.courtdata.dces.util.ContributionFileUtil;
import gov.uk.courtdata.enums.ConcorContributionStatus;
import gov.uk.courtdata.dces.mapper.ContributionFileMapper;
import gov.uk.courtdata.entity.ConcorContributionsEntity;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.repository.ConcorContributionsRepository;
import gov.uk.courtdata.repository.ContributionFileErrorsRepository;
import gov.uk.courtdata.repository.ContributionFilesRepository;
import gov.uk.courtdata.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConcorContributionsService {

    private final ConcorContributionsRepository concorRepository;
    private final ContributionFileMapper contributionFileMapper;
    private final DebtCollectionRepository debtCollectionRepository;
    private final ContributionFilesRepository contributionFilesRepository;
    private final ContributionFileErrorsRepository contributionFileErrorsRepository;

    public List<ConcorContributionResponse> getConcorContributionFiles(ConcorContributionStatus status) {
        log.info("Getting concor contribution file with status with the -> {}", status);
        final List<ConcorContributionsEntity> concorFileList = concorRepository.findByStatus(status);

        return concorFileList.stream().map( cc -> ConcorContributionResponse.builder()
                        .concorContributionId(cc.getId())
                        .xmlContent(cc.getCurrentXml())
                        .build()).toList();
    }

    @Transactional(rollbackFor = MAATCourtDataException.class)
    public boolean createContributionAndUpdateConcorStatus(CreateContributionFileRequest contributionRequest){

        ValidationUtils.isNull(contributionRequest,"contributionRequest object is null");
        ValidationUtils.isEmptyOrHasNullElement(contributionRequest.getConcorContributionIds(),"ContributionIds are empty/null.");

        final ContributionFilesEntity contributionFilesEntity = createContributionFile(contributionRequest);
        return updateConcorStatusForContribution(contributionRequest.getConcorContributionIds(), SENT, contributionFilesEntity.getId());
    }

    @Transactional(rollbackFor =  MAATCourtDataException.class)
    public boolean logContributionProcessed(LogContributionProcessedRequest request){
        boolean successful = false;
        Optional<ConcorContributionsEntity> optionalConcorEntry = concorRepository.findById(request.getConcorId());
        if(optionalConcorEntry.isPresent()) {
            ConcorContributionsEntity concorEntity = optionalConcorEntry.get();
            log.info("Contribution found: {}", concorEntity.getId());
            successful = updateContributionFile(concorEntity.getContribFileId());
            // check if error
            if(!StringUtils.isEmpty(request.getErrorText()) ){
                successful = successful && saveErrorMessage(request, concorEntity);
            }

        }
        return successful;
    }

    private boolean updateContributionFile(Integer fileId){
        if(Objects.isNull(fileId)){
            log.info("No associated file was found for contribution");
            return false;
        }
        Optional<ContributionFilesEntity> optionalEntity = contributionFilesRepository.findById(fileId);
        boolean success = false;
        if ( optionalEntity.isPresent()){
            ContributionFilesEntity filesEntity = optionalEntity.get();
            filesEntity.incrementReceivedCount();
            LocalDate currentDate = LocalDate.now();
            filesEntity.setDateReceived(currentDate);
            filesEntity.setDateModified(currentDate);
            filesEntity.setUserModified("DCES");
            contributionFilesRepository.save(filesEntity);
            success = true;
            log.info("Update of file id : {} successful", fileId);
        }
        return success;
    }

    private boolean saveErrorMessage(LogContributionProcessedRequest request, ConcorContributionsEntity concorEntity){
        contributionFileErrorsRepository.save(ContributionFileUtil.buildContributionFileError(request,concorEntity));
        return true;
    }

    private ContributionFilesEntity createContributionFile(CreateContributionFileRequest contributionRequest) {

        final ContributionFilesEntity contributionFileEntity;
        try {
            log.info("Updating the concor contribution file ref  -> {}", contributionRequest);
            ContributionFileUtil.assessFilename(contributionRequest);
            contributionFileEntity = contributionFileMapper.toContributionFileEntity(contributionRequest);
            debtCollectionRepository.save(contributionFileEntity);

        } catch (Exception e) {
            throw new MAATCourtDataException("Failed to map ConcorContributionRequest to ContributionFilesEntity and persist: [%s]".formatted(e.getMessage()));
        }
        return contributionFileEntity;
    }

    private boolean updateConcorStatusForContribution(Set<Integer> ids, ConcorContributionStatus status, final Integer contributionFileId ){

        final List<ConcorContributionsEntity> concorFileList = concorRepository.findByIdIn(ids);

        log.info("Concor Contributions for status update - count {}", concorFileList.size());
        concorFileList.forEach(cc -> {
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