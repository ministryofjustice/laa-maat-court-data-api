package gov.uk.courtdata.dces.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
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
}