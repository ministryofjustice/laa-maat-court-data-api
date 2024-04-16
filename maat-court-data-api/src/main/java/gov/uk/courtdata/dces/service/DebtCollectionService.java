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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DebtCollectionService {

    private final DebtCollectionRepository debtCollectionRepository;
    private final ContributionFileErrorsRepository contributionFileErrorsRepository;
    private final ContributionFilesRepository contributionFilesRepository;

    public List<String> getContributionFiles(final LocalDate fromDate, final LocalDate toDate) {
        log.info("date with the -> {} {}", fromDate, toDate);
        return debtCollectionRepository.getContributionFiles(convertToCorrectDateFormat(fromDate), convertToCorrectDateFormat(toDate));
    }

    public List<String> getFdcFiles(final LocalDate fromDate, final LocalDate toDate) {
        log.info("date with the -> {} {}", fromDate, toDate);
        return debtCollectionRepository.getFdcFiles(convertToCorrectDateFormat(fromDate), convertToCorrectDateFormat(toDate));
    }

    String convertToCorrectDateFormat(LocalDate localDate) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        log.info("Printing before date " + localDate.toString());
        log.info("Printing after date " + localDate.format(dateTimeFormatter));
        return localDate.format(dateTimeFormatter);
    }

    public boolean saveError(ContributionFileErrorsEntity entity){
        contributionFileErrorsRepository.save(entity);
        return true;
    }


    public boolean updateContributionFileReceivedCount(Integer fileId){
        if(Objects.isNull(fileId)){
            log.info("No associated file was found for contribution");
            return false;
        }
        ContributionFilesEntity filesEntity = getContributionFile(fileId);
        if ( Objects.nonNull(filesEntity)){
            filesEntity.incrementReceivedCount();
            filesEntity.setDateReceived(LocalDate.now());
            debtCollectionRepository.updateContributionFilesEntity(filesEntity);
            log.info("Update of file id : {} successful", fileId);
            return true;
        }
        else {
            throw new MAATCourtDataException("No file was found for the fdc.");
        }
    }

    private ContributionFilesEntity getContributionFile(int fileId){
        Optional<ContributionFilesEntity> optionalEntity = contributionFilesRepository.findById(fileId);
        if(optionalEntity.isPresent()){
            return optionalEntity.get().toBuilder().build();
            // Saving should be done via the JDBCTemplate, not JPA.
            // So in order to avoid JPA @Transaction saving, in addition to the JDBCTemplate,
            // we need to clone the object.
        }
        return null;
    }


}
