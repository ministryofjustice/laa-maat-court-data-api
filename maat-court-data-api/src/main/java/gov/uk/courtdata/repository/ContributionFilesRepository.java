package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.ContributionFilesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContributionFilesRepository extends JpaRepository<ContributionFilesEntity, Integer> {


    List<ContributionFilesEntity> getByFileNameLikeAndDateCreatedBetweenOrderByFileId(String filename, LocalDate fromDate, LocalDate toDate);

    @Modifying
    @Query("""
           UPDATE ContributionFilesEntity cf
           SET cf.recordsReceived = COALESCE(cf.recordsReceived, 0) + 1,
               cf.dateReceived = CURRENT_DATE,
               cf.dateModified = CURRENT_DATE,
               cf.userModified = :userModified
           WHERE cf.fileId = :id""")
    int incrementRecordsReceived(@Param("id") Integer id,
                                 @Param("userModified") String userModified);
}
