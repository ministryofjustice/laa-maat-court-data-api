package gov.uk.courtdata.integration.prosecution_concluded;

import lombok.RequiredArgsConstructor;
import org.h2.tools.SimpleResultSet;

import javax.persistence.Column;
import java.sql.*;

@RequiredArgsConstructor
public final class H2StoredProcedures {

    public static ResultSet updateCcOutcome(Integer repId,
                                      String ccOutcome,
                                      String benchWarrantIssued,
                                      String appealType,
                                      String imprisoned,
                                      String caseNumber,
                                      String crownCourtCode) throws SQLException {

        SimpleResultSet rs = new SimpleResultSet();
        String sqlInsert =
                "INSERT INTO TOGDATA.UPDATE_OUTCOMES " +
                "(REP_ID,CC_OUTCOME,BENCH_WARRANT_ISSUED,APPEAL_TYPE,IMPRISONED,CASE_NUMBER,CROWN_COURT_CODE)" +
                String.format(
                        "VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                        repId, ccOutcome, benchWarrantIssued, appealType, imprisoned, caseNumber, crownCourtCode);
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:mla", "sa", "");
        Statement statement = connection.createStatement();
        statement.execute(sqlInsert);
        return rs;

    }



}
