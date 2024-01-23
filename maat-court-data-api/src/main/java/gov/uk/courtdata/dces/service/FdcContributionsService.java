package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.dces.mapper.FdcContributionsMapper;
import gov.uk.courtdata.dces.request.FdcContributionsRequest;
import gov.uk.courtdata.dces.response.FdcContributionsResponse;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.entity.FdcContributionsEntity;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.repository.FdcContributionsRepository;
import gov.uk.courtdata.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static gov.uk.courtdata.enums.FdcContributionsStatus.SENT;

@Slf4j
@Service
@RequiredArgsConstructor
public class FdcContributionsService {
    private static final String DB_USER_NAME = "DCES";
    private final FdcContributionsRepository fdcContributionsRepository;
    private final FdcContributionsMapper fdcContributionsMapper;
    private final DebtCollectionRepository debtCollectionRepository;

    public List<FdcContributionsResponse> getFdcContributionFiles(FdcContributionsStatus status) {
        log.info("Getting fdc contribution file with status with the -> {}", status);
        final List<FdcContributionsEntity> fdcFileList = fdcContributionsRepository.findByStatus(status);

        return fdcFileList.stream().map( cc -> FdcContributionsResponse.builder()
                        .id(cc.getId())
                        .finalCost(cc.getFinalCost())
                        .dateCalculated(cc.getDateCalculated())
                        .lgfsCost(cc.getLgfsCost())
                        .agfsCost(cc.getAgfsCost())
                        .sentenceOrderDate(cc.getRepOrderEntity().getSentenceOrderDate())
                        .build()).toList();
    }

    @Transactional(rollbackFor = MAATCourtDataException.class)
    public boolean createContributionAndUpdateConcorStatus(FdcContributionsRequest fdcContributionRequest){

        ValidationUtils.isNull(fdcContributionRequest,"fdcContributionRequest object is null");
        ValidationUtils.isEmptyOrHasNullElement(fdcContributionRequest.getConcorContributionIds(),"ContributionIds are empty/null.");

        final ContributionFilesEntity contributionFilesEntity = createContributionFile(fdcContributionRequest);
        return updateConcorStatusForContribution(fdcContributionRequest.getConcorContributionIds(), SENT, contributionFilesEntity.getId());
    }

    private ContributionFilesEntity createContributionFile(FdcContributionsRequest fdcContributionsRequest) {

        final ContributionFilesEntity contributionsFileEntity;
        try {
            log.info("Updating the fdc contribution file ref  -> {}", fdcContributionsRequest);
            contributionsFileEntity = fdcContributionsMapper.toFdcContributionsEntity(fdcContributionsRequest);
            // when file is not provided then xml generate.
            contributionsFileEntity.setFileName(Optional.ofNullable(fdcContributionsRequest.getXmlFileName()).orElse(getFilename()));
            contributionsFileEntity.setUserCreated(DB_USER_NAME);
            debtCollectionRepository.save(contributionsFileEntity);

        } catch (Exception e) {
            throw new MAATCourtDataException("Failed to map FdcContributionsRequest to FdcContributionsEntity and persist: [%s]".formatted(e.getMessage()));
        }
        return contributionsFileEntity;
    }

    private boolean updateConcorStatusForContribution(Set<Integer> ids, FdcContributionsStatus status, final Integer contributionFileId ){

        final List<FdcContributionsEntity> concorFileList = fdcContributionsRepository.findByIdIn(ids);

        log.info("Fdc Contributions for status update - count {}", concorFileList.size());
        concorFileList.forEach(cc -> cc.setStatus(status));

        if (!concorFileList.isEmpty()) {
            fdcContributionsRepository.saveAll(concorFileList);
            log.info("Updated all fdc contribution file for contributionFileId {} and count {}",
                    contributionFileId, concorFileList.size());
            return true;
        } else {
            log.info("Fdc Contributions files empty {}", ids);
            return false;
        }
    }

    @NotNull
    private static String getFilename() {
        final LocalDateTime date = LocalDateTime.now();
        final String filename = date.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        final StringBuilder stringBuilder = new StringBuilder("FDC_");
        stringBuilder.append(filename);
        stringBuilder.append(".xml");
        log.info("Contribution file name {}", stringBuilder);
        return stringBuilder.toString();
    }
}