package gov.uk.courtdata.repository;

import gov.uk.courtdata.data.convertor.ApplicationConvertor;
import gov.uk.courtdata.data.convertor.UserSessionConvertor;
import gov.uk.courtdata.data.oracle.*;
import gov.uk.courtdata.dto.application.ApplicationDTO;
import gov.uk.courtdata.dto.application.UserDTO;
import gov.uk.courtdata.model.StoredProcedureRequest;
import gov.uk.courtdata.validator.MAATApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;


@Repository
@Slf4j
public class StoredProcedureRepository {

    private static final String PACKAGE_NAME_NOT_SET_EXCEPTION = "The package name has not been set";
    @PersistenceContext
    private EntityManager entityManager;

    public ApplicationDTO executeStoredProcedure(StoredProcedureRequest storedProcedure) {

        ApplicationDTO result = null;

        try {
            Connection conn = entityManager.unwrap(Connection.class);
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
            throw new RuntimeException(exception);
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

    protected CallableStatement getDbProcedureStatement(Connection connection, StoredProcedureRequest callStoredProcedure) throws SQLException,
            MAATApplicationException {
        if (StringUtils.isBlank(callStoredProcedure.getDbPackageName())) {
            throw new MAATApplicationException(PACKAGE_NAME_NOT_SET_EXCEPTION);
        } else if ((StringUtils.isBlank(callStoredProcedure.getProcedureName()))) {
            throw new MAATApplicationException(PACKAGE_NAME_NOT_SET_EXCEPTION);
        }
        return connection.prepareCall("{ call " + callStoredProcedure.getDbPackageName()
                + "." + callStoredProcedure.getProcedureName() + "(?)}");
    }
}
