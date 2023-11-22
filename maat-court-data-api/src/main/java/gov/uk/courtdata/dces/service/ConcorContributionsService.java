package gov.uk.courtdata.dces.service;

import static gov.uk.courtdata.enums.ConcorContributionStatus.SENT;

import gov.uk.courtdata.enums.ConcorContributionStatus;
import gov.uk.courtdata.dces.mapper.ContributionFileMapper;
import gov.uk.courtdata.dces.request.ConcorContributionRequest;
import gov.uk.courtdata.entity.ConcorContributionsEntity;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.repository.ConcorContributionsRepository;
import gov.uk.courtdata.repository.ContributionFilesRepository;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class ConcorContributionsService {
    private static final String DB_USER_NAME = "MLA";
    private final ConcorContributionsRepository concorRepository;
    private final ContributionFilesRepository contributionFileRepository;
    private final ContributionFileMapper contributionFileMapper;

    public List<String> getConcorFiles(final ConcorContributionStatus status) {
        log.info("Getting concor contribution file with status with the -> {}", status);
        final List<ConcorContributionsEntity> concorFileList = concorRepository.findByStatus(status);
        return concorFileList.stream().map(ConcorContributionsEntity::getCurrentXml).toList();
    }

    @Transactional(rollbackFor = MAATCourtDataException.class)
    public boolean createContributionAndUpdateConcorStatus(final ConcorContributionRequest contributionRequest){

        ValidationUtils.isNull(contributionRequest,"contributionRequest object is null");
        ValidationUtils.isEmpty(contributionRequest.getContributionIds(),"ContributionIds are empty/null.");

        final ContributionFilesEntity contributionFilesEntity = createContributionFile(contributionRequest);
        return updateConcorStatusForContribution(contributionRequest.getContributionIds(), SENT, contributionFilesEntity.getId());
    }

    private ContributionFilesEntity createContributionFile(final ConcorContributionRequest contributionRequest) {

        final ContributionFilesEntity contributionFileEntity;
        try {
            log.info("Updating the concor contribution file ref  -> {}", contributionRequest);
            contributionFileEntity = contributionFileMapper.toContributionFileEntity(contributionRequest);
            // when file is not provided then xml generate.
            contributionFileEntity.setFileName(Optional.ofNullable(contributionRequest.getXmlFileName()).orElse(getFilename()));
            contributionFileEntity.setUserCreated(DB_USER_NAME);
            contributionFileEntity.setUserModified(DB_USER_NAME);

            contributionFileRepository.save(contributionFileEntity);
        } catch (Exception e) {
            throw new MAATCourtDataException("Failed to map ConcorContributionRequest to ContributionFilesEntity and persist: [%s]".formatted(e.getMessage()));
        }
        return contributionFileEntity;
    }

    private boolean updateConcorStatusForContribution(final Set<String> ids, final ConcorContributionStatus status, final Integer contributionFileId ){

        final List<ConcorContributionsEntity> concorFileList = concorRepository.findByIdIn(ids);

        log.info("Concor Contributions for status update - count {}", concorFileList.size());
        concorFileList.forEach(cc -> {
            cc.setStatus(status);
            cc.setContribFileId(contributionFileId);
            cc.setUserModified("TOGDATA");
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