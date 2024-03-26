package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.entity.ContributionFileErrorsEntity;
import gov.uk.courtdata.repository.ContributionFileErrorsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DebtCollectionService {

    private final DebtCollectionRepository debtCollectionRepository;
    private final ContributionFileErrorsRepository contributionFileErrorsRepository;

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

}
