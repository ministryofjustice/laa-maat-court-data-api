package gov.uk.courtdata.dces.service;

import static gov.uk.courtdata.enums.ConcorContributionStatus.SENT;

import gov.uk.courtdata.dces.request.CreateContributionFileRequest;
import gov.uk.courtdata.dces.response.ConcorContributionResponse;
import gov.uk.courtdata.dces.util.ContributionFileUtil;
import gov.uk.courtdata.enums.ConcorContributionStatus;
import gov.uk.courtdata.dces.mapper.ContributionFileMapper;
import gov.uk.courtdata.entity.ConcorContributionsEntity;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.repository.ConcorContributionsRepository;
import gov.uk.courtdata.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConcorContributionsService {

    private final ConcorContributionsRepository concorRepository;
    private final ContributionFileMapper contributionFileMapper;
    private final DebtCollectionRepository debtCollectionRepository;

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

    @NotNull
    private static String getFilename() {
        final LocalDateTime date = LocalDateTime.now();
        final String filename = date.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        final StringBuilder stringBuilder = new StringBuilder("CONTRIBUTIONS_");
        stringBuilder.append(filename);
        stringBuilder.append(".xml");
        log.info("Contribution file name {}", stringBuilder);
        return stringBuilder.toString();
    }
}