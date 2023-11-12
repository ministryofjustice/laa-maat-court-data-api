package gov.uk.courtdata.dces.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dces.request.ConcorContributionRequest;
import gov.uk.courtdata.dces.enums.ConcorContributionStatus;
import gov.uk.courtdata.dces.mapper.ContributionFileMapper;
import gov.uk.courtdata.entity.ConcorContributionsEntity;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.repository.ConcorContributionsRepository;
import gov.uk.courtdata.repository.ContributionFilesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@XRayEnabled
@RequiredArgsConstructor
public class ConcorContributionsService {
    private static final String DB_USER_NAME = "MLA";
    private final ConcorContributionsRepository concorRepository;
    private final ContributionFilesRepository contributionFileRepository;
    private final ContributionFileMapper contributionFileMapper;

    @Transactional
    public void createContributionFileAndUpdateConcorContributionsStatus(final ConcorContributionRequest dto) {
        if (Objects.isNull(dto)) {
            throw new MAATCourtDataException("ConcorContributionRequest is null.");
        }
        ContributionFilesEntity contributionFileEntity;
        try {
            log.info("updating the concor contribution file ref  -> {}", dto);
            contributionFileEntity = contributionFileMapper.toContributionFileEntity(dto);

            contributionFileEntity.setId(getMaxAvailableId());
            contributionFileEntity.setFileName(getFilename());
            contributionFileEntity.setDateCreated(LocalDate.now());
            contributionFileEntity.setUserCreated(DB_USER_NAME);
            contributionFileEntity.setUserModified(DB_USER_NAME);

            contributionFileRepository.save(contributionFileEntity);
        } catch (Exception e) {
            throw new MAATCourtDataException("ConcorContributionRequest to ContributionEntity Mapper error.");
        }

        final List<ConcorContributionsEntity> concorFileList = concorRepository.findByStatus(ConcorContributionStatus.ACTIVE);

        log.info("Contribution with active status - count {}", concorFileList.size());
        concorFileList.forEach(cc -> {
            cc.setStatus((ConcorContributionStatus.SENT));
            cc.setContribFileId(contributionFileEntity.getId());
            cc.setDateModified(LocalDate.now());
            cc.setUserModified("TOGDATA");
        });

        log.info("Updated all concor contribution files for  contrib_file_id {} and count {}",
                contributionFileEntity.getId(), concorFileList.size());

        if (!concorFileList.isEmpty()) {
            concorRepository.saveAll(concorFileList);
        }
    }

    public List<String> getConcorFiles(final ConcorContributionStatus status) {
        log.info("status with the -> {}", status);
        List<ConcorContributionsEntity> concorFileList = concorRepository.findByStatus(status);
        return concorFileList.stream().map(ConcorContributionsEntity::getCurrentXml).collect(Collectors.toList());
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

    public int getMaxAvailableId() {
        return contributionFileRepository.findMaxId() + 1;
    }
}