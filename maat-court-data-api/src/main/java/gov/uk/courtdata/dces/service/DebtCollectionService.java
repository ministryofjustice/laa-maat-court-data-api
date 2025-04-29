package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.entity.ContributionFileErrorsEntity;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.repository.ContributionFileErrorsRepository;
import gov.uk.courtdata.repository.ContributionFilesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DebtCollectionService {
    private static final String USER_AUDIT = "DCES";
    private final DebtCollectionRepository debtCollectionRepository;
    private final ContributionFileErrorsRepository contributionFileErrorsRepository;
    private final ContributionFilesRepository contributionFilesRepository;

    public List<String> getContributionFiles(LocalDate fromDate, LocalDate toDate) {
        log.info("date range -> {} {}", fromDate, toDate);
        List<ContributionFilesEntity> contributionFilesList = contributionFilesRepository.getByFileNameLikeAndDateCreatedBetweenOrderByFileId("CONTRIBUTIONS%", fromDate, toDate);
        return contributionFilesList.stream().map(ContributionFilesEntity::getXmlContent).toList();
    }

    public List<String> getFdcFiles(LocalDate fromDate, LocalDate toDate) {
        log.info("date range -> {} {}", fromDate, toDate);
        List<ContributionFilesEntity> contributionFilesList = contributionFilesRepository.getByFileNameLikeAndDateCreatedBetweenOrderByFileId("FDC%", fromDate, toDate);
        return contributionFilesList.stream().map(ContributionFilesEntity::getXmlContent).toList();
    }

    public void saveError(ContributionFileErrorsEntity entity) {
        contributionFileErrorsRepository.save(entity);
    }

    public boolean updateContributionFileReceivedCount(Integer fileId) {
        if (Objects.isNull(fileId)) {
            log.info("No associated file was found for contribution");
            return false;
        }
        int rowsUpdated = contributionFilesRepository.incrementRecordsReceived(fileId, USER_AUDIT);
        log.info("update contribution_file ID {} records received: {} row(s) updated", fileId, rowsUpdated);
        if (rowsUpdated != 1) {
            throw new MAATCourtDataException("update contribution_file ID " + fileId + " records received: " + rowsUpdated + " row(s) updated");
        }
        return true;
    }

    private ContributionFilesEntity getContributionFile(int fileId) {
        Optional<ContributionFilesEntity> optionalEntity = contributionFilesRepository.findById(fileId);
        if (optionalEntity.isPresent()) {
            return optionalEntity.get().toBuilder().build();
            // Saving should be done via the JDBCTemplate, not JPA.
            // So in order to avoid JPA @Transaction saving, in addition to the JDBCTemplate,
            // we need to clone the object.
        }
        return null;
    }
}
