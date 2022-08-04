package gov.uk.courtdata.integration.prosecution_concluded.procedures;

import lombok.RequiredArgsConstructor;
import org.h2.tools.SimpleResultSet;
import org.springframework.data.repository.query.Param;

import java.sql.*;
import java.time.LocalDate;

@RequiredArgsConstructor
public final class H2StoredProcedures {

    public static ResultSet updateCcOutcome(Integer repId,
                                      String ccOutcome,
                                      String benchWarrantIssued,
                                      String appealType,
                                      String imprisoned,
                                      String caseNumber,
                                      String crownCourtCode) throws SQLException {

        String sqlInsert =
                "INSERT INTO TOGDATA.UPDATE_OUTCOMES " +
                "(REP_ID,CC_OUTCOME,BENCH_WARRANT_ISSUED,APPEAL_TYPE,IMPRISONED,CASE_NUMBER,CROWN_COURT_CODE)" +
                String.format(
                        "VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                        repId, ccOutcome, benchWarrantIssued, appealType, imprisoned, caseNumber, crownCourtCode);
        return runSqlStatement(sqlInsert);

    }

    public static ResultSet updateAppealSentenceOrdDt(Integer repId,
                                                      String dbUser,
                                                      LocalDate sentenceOrderDate,
                                                      LocalDate dateChanged) throws SQLException {

        String sqlInsert =
                "INSERT INTO TOGDATA.UPDATE_APPEAL_SENTENCE " +
                        "(REP_ID,DB_USER,SENTENCE_DATE,DATE_CHANGED)" +
                        String.format(
                                "VALUES (%d, '%s', '%s', '%s');",
                                repId, dbUser, sentenceOrderDate, dateChanged);
        return runSqlStatement(sqlInsert);

    }

    public static ResultSet updateCrownCourtSentenceOrdDt(Integer repId, String dbUser, LocalDate sentenceOrderDate) throws SQLException {

        String sqlInsert =
                "INSERT INTO TOGDATA.UPDATE_CC_SENTENCE " +
                        "(REP_ID,DB_USER,SENTENCE_DATE)" +
                        String.format(
                                "VALUES (%d, '%s', '%s');",
                                repId, dbUser, sentenceOrderDate);
        return runSqlStatement(sqlInsert);

    }

    private static ResultSet runSqlStatement(String sqlStatement) throws SQLException {
        SimpleResultSet rs = new SimpleResultSet();
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:mla", "sa", "");
        Statement statement = connection.createStatement();
        statement.execute(sqlStatement);
        return rs;
    }



}
