package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.ContributionFilesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContributionFilesRepository extends JpaRepository<ContributionFilesEntity, Integer> {

    @Query(value = "SELECT cf.xml_content FROM TOGDATA.CONTRIBUTION_FILES CF WHERE CF.FILE_NAME like '%%CONTRIBUTIONS%%' AND CF.DATE_CREATED BETWEEN fromDate and toDate",
            nativeQuery = true)
    List<String> getContribution(LocalDate fromDate, LocalDate toDate);

    @Query(value = "SELECT cf.xml_content FROM TOGDATA.CONTRIBUTION_FILES CF WHERE CF.FILE_NAME like '%%FDC%%' AND CF.DATE_CREATED BETWEEN fromDate and toDate",
            nativeQuery = true)
    List<String> getFDC(LocalDate fromDate, LocalDate toDate);
}
