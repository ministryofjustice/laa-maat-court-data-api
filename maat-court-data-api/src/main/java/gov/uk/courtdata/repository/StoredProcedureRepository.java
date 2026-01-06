package gov.uk.courtdata.repository;

import gov.uk.courtdata.dao.convertor.ApplicationConvertor;
import gov.uk.courtdata.dao.convertor.UserSessionConvertor;
import gov.uk.courtdata.dao.oracle.*;
import gov.uk.courtdata.dto.application.ApplicationDTO;
import gov.uk.courtdata.dto.application.UserDTO;
import gov.uk.courtdata.model.StoredProcedureRequest;
import gov.uk.courtdata.validator.MAATApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Pattern;

@Slf4j
@Repository
public class StoredProcedureRepository {

    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.togdata.username}")
    private String dbUsername;
    @Value("${spring.togdata.password}")
    private String dbPassword;

    private static final String PACKAGE_NAME_NOT_SET_EXCEPTION = "The package name has not been set";
    private static final String PROCEDURE_NAME_NOT_SET_EXCEPTION = "The procedure name has not been set";
    private static final int MIN_SQL_ERROR_CODE = 20000;
    private static final int MAX_SQL_ERROR_CODE = 21999;
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("^[A-Za-z0-9_]+$");


    public ApplicationDTO executeStoredProcedure(StoredProcedureRequest storedProcedure) throws
            MAATApplicationException, SQLException {

        Connection conn = null;
        ApplicationDTO result = null;


        try {
            conn = getConnectionFromDriverManager();
            conn.setAutoCommit(false);
            setUserSession(conn, storedProcedure.getUser());

            HashMap<String, Class<?>> typeDTOMap = new HashMap<>();
            typeDTOMap.put(ApplicationType._SQL_NAME, ApplicationType.class);
            typeDTOMap.put(RepStatusType._SQL_NAME, RepStatusType.class);
            typeDTOMap.put(CaseTypeType._SQL_NAME, CaseTypeType.class);
            typeDTOMap.put(MagsCourtType._SQL_NAME, MagsCourtType.class);
            typeDTOMap.put(OutcomeType._SQL_NAME, OutcomeType.class);
            typeDTOMap.put(ApplicantDetailsType._SQL_NAME, ApplicantDetailsType.class);
            typeDTOMap.put(SupplierType._SQL_NAME, SupplierType.class);
            typeDTOMap.put(LSCTransferType._SQL_NAME, LSCTransferType.class);
            typeDTOMap.put(AreaTransferType._SQL_NAME, AreaTransferType.class);
            typeDTOMap.put(AssessmentType._SQL_NAME, AssessmentType.class);
            typeDTOMap.put(AssessmentSummaryTabType._SQL_NAME, AssessmentSummaryTabType.class);
            typeDTOMap.put(HardshipOverviewType._SQL_NAME, HardshipOverviewType.class);
            typeDTOMap.put(CrownCourtOverviewType._SQL_NAME, CrownCourtOverviewType.class);
            typeDTOMap.put(CapitalEquityType._SQL_NAME, CapitalEquityType.class);
            typeDTOMap.put(AddressType._SQL_NAME, AddressType.class);
            typeDTOMap.put(PropertyType._SQL_NAME, PropertyType.class);
            conn.setTypeMap(typeDTOMap);

            try (CallableStatement statement = getDbProcedureStatement(conn, storedProcedure)) {
                ApplicationConvertor convertor = new ApplicationConvertor();
                convertor.setTypeFromDTO(storedProcedure.getApplication());

                statement.setObject(1, convertor.getOracleType());
                statement.registerOutParameter(1, ApplicationType._SQL_TYPECODE, ApplicationType._SQL_NAME);
                statement.execute();

                if (statement.getObject(1, typeDTOMap) == null) {
                    log.debug("no application returned ");
                } else {
                    Object obj = statement.getObject(1);
                    convertor.setDTOFromType(obj);
                    result = convertor.getDTO();
                }
            }
        } catch (Exception exception) {
            handleException(exception, conn);
            throw new RuntimeException(exception);
        } finally {
            closeAndCommitConnection(conn);
        }
        return result;
    }

    private void setUserSession(Connection connection, UserDTO userDto) throws MAATApplicationException {

        try {
            UserSessionConvertor userConvertor = new UserSessionConvertor();
            userConvertor.setTypeFromDTO(userDto);
            UserSessionType userSessionType = (UserSessionType) userConvertor.getDbType();

            HashMap<String, Class<?>> typeDTOMap = new HashMap<>();
            typeDTOMap.put(UserSessionType._SQL_NAME, UserSessionType.class);
            typeDTOMap.put(UserRoleType._SQL_NAME, UserRoleType.class);
            typeDTOMap.put(UserRoleTabType._SQL_NAME, UserRoleTabType.class);
            connection.setTypeMap(typeDTOMap);


            try (CallableStatement statement = connection.prepareCall("{call user_admin.set_user_session( ? )}")) {
                statement.setObject(1, userSessionType);
                statement.registerOutParameter(1, UserSessionType._SQL_TYPECODE, UserSessionType._SQL_NAME);
                statement.execute();
            }
        } catch (Exception exception) {
            throw new MAATApplicationException(exception);
        }

    }

    protected CallableStatement getDbProcedureStatement(Connection connection, StoredProcedureRequest request)
            throws SQLException, MAATApplicationException {

        String packageName = request.getDbPackageName();
        String procedureName = request.getProcedureName();

        if (StringUtils.isBlank(request.getDbPackageName())) {
            throw new MAATApplicationException(PACKAGE_NAME_NOT_SET_EXCEPTION);
        }

        if ((StringUtils.isBlank(request.getProcedureName()))) {
            throw new MAATApplicationException(PROCEDURE_NAME_NOT_SET_EXCEPTION);
        }

        // Validate that the identifiers contain only allowed characters
        if (!IDENTIFIER_PATTERN.matcher(packageName).matches()) {
            throw new MAATApplicationException("Invalid package name");
        }

        if (!IDENTIFIER_PATTERN.matcher(procedureName).matches()) {
            throw new MAATApplicationException("Invalid procedure name");
        }

        String sql = String.format("{ call %s.%s( ? )}", packageName, procedureName);

        return connection.prepareCall(sql);
    }


    private Connection getConnectionFromDriverManager() throws MAATApplicationException {
        Connection wantedConnection;

        try {
            wantedConnection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (SQLException exception) {
            log.error("Connection request failed from driver manager", exception);
            log.error(exception.getMessage());
            throw new MAATApplicationException("Could not retrieve a connection to the database");
        }

        return wantedConnection;
    }

    void closeAndCommitConnection(Connection connection) throws MAATApplicationException {
        try (Connection autoClosableConnection = connection) {
            if (isConnectionOpen(autoClosableConnection)) {
                commitTransactionalConnection(autoClosableConnection);
            }
        } catch (Exception e) {
            throw new MAATApplicationException("Could not close the Connection object", e);
        }
    }

    private boolean isConnectionOpen(Connection connection) throws SQLException {
        return connection != null && !connection.isClosed();
    }

    private void commitTransactionalConnection(Connection connection) throws SQLException {
        try {
            if (connectionRequiresManualCommit(connection)) {
                connection.commit();
            }
        } catch (SQLException se) {
            log.error(getClass().getName() + " ### Cannot commit transaction ");
            throw se;
        }
    }

    private boolean connectionRequiresManualCommit(Connection connection) throws SQLException {
        return !connection.getAutoCommit();
    }

    private void rollbackTransactionalConnection(Connection connection) throws SQLException {
        try {
            if (connectionRequiresManualCommit(connection)) {
                connection.rollback();
            }
        } catch (SQLException se) {
            log.error(getClass().getName() + " ### Cannot rollback transaction ");
            throw se;
        }
    }

    public void handleException(Throwable t, Connection conn) throws SQLException, MAATApplicationException {
        log.error("### " + t);

        rollbackTransactionalConnection(conn);
        if (t instanceof SQLException sqlException) {
            int errorCode = sqlException.getErrorCode();
            if ((errorCode >= MIN_SQL_ERROR_CODE) && (errorCode <= MAX_SQL_ERROR_CODE)) {
                String errorMessage = sqlException.getMessage();
                //remove stack trace codes from exception
                int start = errorMessage.indexOf(':');
                int end = errorMessage.indexOf('\n');
                if (start > 0 && end > 0) {
                    errorMessage = "Application Error " + errorMessage.substring(start, end);
                }
                throw new MAATApplicationException(errorMessage, sqlException);
            }
        }
    }

}
