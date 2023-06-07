package gov.uk.courtdata.dces.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.repository.ContributionFilesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@XRayEnabled
public class DebtCollectionService {

    private final ContributionFilesRepository contributionFilesRepository;

    public List<String> getContributionFiles(final LocalDate fromDate, final LocalDate toDate) {

        List<String> contributionFileList = contributionFilesRepository.getContribution(fromDate, toDate);
        return contributionFileList;

    }

    public List<String> getFDC(final LocalDate fromDate,final LocalDate toDate){

        List<String> contributionFileList = contributionFilesRepository.getFDC(fromDate, toDate);
        return contributionFileList;

    }
}
