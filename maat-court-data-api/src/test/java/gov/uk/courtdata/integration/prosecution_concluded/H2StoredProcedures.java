package gov.uk.courtdata.integration.prosecution_concluded;

import org.h2.tools.SimpleResultSet;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public final class H2StoredProcedures {

    private H2StoredProcedures() {
        
    }

    public static ResultSet updateCcOutcome(Integer repId,
                                      String ccOutcome,
                                      String benchWarrantIssued,
                                      String appealType,
                                      String imprisoned,
                                      String caseNumber,
                                      String crownCourtCode) throws SQLException {

        SimpleResultSet rs = new SimpleResultSet();

        return rs;

    }
}
