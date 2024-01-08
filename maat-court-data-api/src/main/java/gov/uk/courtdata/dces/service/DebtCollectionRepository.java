package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.entity.ContributionFilesEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.sql.Clob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DebtCollectionRepository {

    private final JdbcTemplate jdbcTemplate;

    List<String> getContributionFiles(final String fromDate, final String toDate) {
        String query = "SELECT cf.xml_content FROM TOGDATA.CONTRIBUTION_FILES CF " +
                "WHERE CF.FILE_NAME LIKE '%%CONTRIBUTIONS%%' " +
                "AND TO_DATE(CF.DATE_CREATED) BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') Order by CF.ID ASC";

        return jdbcTemplate.queryForList(query, String.class, fromDate, toDate);
    }

    List<String> getFdcFiles(final String fromDate, final String toDate) {
        String query = "SELECT cf.xml_content FROM TOGDATA.CONTRIBUTION_FILES CF " +
                "WHERE CF.FILE_NAME LIKE '%%FDC%%' " +
                "AND TO_DATE(CF.DATE_CREATED) BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') Order by CF.ID ASC";

        return jdbcTemplate.queryForList(query, String.class, fromDate, toDate);
    }

    public boolean save(ContributionFilesEntity contributionFileEntity) {

        log.info("Inserting into TOGDATA.CONTRIBUTION_FILES using jdbcTemplate");
        return insertingTableRow(contributionFileEntity);
    }

    public boolean insertingTableRow(ContributionFilesEntity contributionFileEntity) {
        jdbcTemplate.execute(
                (ConnectionCallback<Object>) con -> {

                    String insertingSQL = "INSERT INTO TOGDATA.CONTRIBUTION_FILES (ID, FILE_NAME, RECORDS_SENT, DATE_CREATED, USER_CREATED, XML_CONTENT, ACK_XML_CONTENT) " +
                            "VALUES (TOGDATA.S_GENERAL_SEQUENCE.NEXTVAL, ?, ?, ?, ?, XMLType(?), XMLType(?))";

                    try (PreparedStatement ps = con.prepareStatement(insertingSQL)) {

                        ps.setString(1, contributionFileEntity.getFileName());
                        ps.setInt(2, contributionFileEntity.getRecordsSent());
                        ps.setDate(3, Date.valueOf(LocalDate.now()));
                        ps.setString(4, contributionFileEntity.getUserCreated());

                        Clob clob = con.createClob();
                        clob.setString(1, contributionFileEntity.getXmlContent());
                        ps.setClob(5, clob);

                        Clob ackXmlContentClob = con.createClob();
                        ackXmlContentClob.setString(1, contributionFileEntity.getAckXmlContent());
                        ps.setClob(6, ackXmlContentClob);

                        return ps.execute();
                    }
                }
        );
        return false;
    }

}