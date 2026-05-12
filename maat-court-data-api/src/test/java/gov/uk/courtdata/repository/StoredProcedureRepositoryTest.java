package gov.uk.courtdata.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.model.StoredProcedureRequest;
import gov.uk.courtdata.validator.MAATApplicationException;

import java.sql.CallableStatement;
import java.sql.Connection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StoredProcedureRepositoryTest {

    private static final String VALID_PACKAGE = "myPackage";
    private static final String VALID_PROCEDURE = "myProcedure";

    @Test
    void testValidInputs() throws Exception {
        // Arrange
        Connection connection = mock(Connection.class);
        CallableStatement callableStatement = mock(CallableStatement.class);
        String expectedSql = "{ call " + VALID_PACKAGE + "." + VALID_PROCEDURE + "( ? )}";
        when(connection.prepareCall(expectedSql)).thenReturn(callableStatement);

        StoredProcedureRequest request = mock(StoredProcedureRequest.class);
        when(request.getDbPackageName()).thenReturn(VALID_PACKAGE);
        when(request.getProcedureName()).thenReturn(VALID_PROCEDURE);

        StoredProcedureRepository repo = new StoredProcedureRepository();

        // Act
        CallableStatement result = repo.getDbProcedureStatement(connection, request);

        // Assert
        assertThat(result).isNotNull();
        verify(connection).prepareCall(expectedSql);
    }

    @Test
    void testBlankPackageName() {
        // Arrange
        Connection connection = mock(Connection.class);
        StoredProcedureRequest request = mock(StoredProcedureRequest.class);
        when(request.getDbPackageName()).thenReturn("   ");
        when(request.getProcedureName()).thenReturn(VALID_PROCEDURE);

        StoredProcedureRepository repo = new StoredProcedureRepository();

        // Act & Assert
        assertThatThrownBy(() -> repo.getDbProcedureStatement(connection, request))
                .isInstanceOf(MAATApplicationException.class)
                .hasMessage("The package name has not been set");
    }

    @Test
    void testBlankProcedureName() {
        // Arrange
        Connection connection = mock(Connection.class);
        StoredProcedureRequest request = mock(StoredProcedureRequest.class);
        when(request.getDbPackageName()).thenReturn(VALID_PACKAGE);
        when(request.getProcedureName()).thenReturn("");

        StoredProcedureRepository repo = new StoredProcedureRepository();

        // Act & Assert
        assertThatThrownBy(() -> repo.getDbProcedureStatement(connection, request))
                .isInstanceOf(MAATApplicationException.class)
                .hasMessage("The procedure name has not been set");
    }

    @Test
    void testInvalidPackageName() {
        // Arrange
        Connection connection = mock(Connection.class);
        StoredProcedureRequest request = mock(StoredProcedureRequest.class);
        // Invalid package name (contains a hyphen)
        when(request.getDbPackageName()).thenReturn("invalid-package");
        when(request.getProcedureName()).thenReturn(VALID_PROCEDURE);

        StoredProcedureRepository repo = new StoredProcedureRepository();

        // Act & Assert
        assertThatThrownBy(() -> repo.getDbProcedureStatement(connection, request))
                .isInstanceOf(MAATApplicationException.class)
                .hasMessage("Invalid package name");
    }

    @Test
    void testInvalidProcedureName() {
        // Arrange
        Connection connection = mock(Connection.class);
        StoredProcedureRequest request = mock(StoredProcedureRequest.class);
        when(request.getDbPackageName()).thenReturn(VALID_PACKAGE);
        // Invalid procedure name (contains a space)
        when(request.getProcedureName()).thenReturn("invalid procedure");

        StoredProcedureRepository repo = new StoredProcedureRepository();

        // Act & Assert
        assertThatThrownBy(() -> repo.getDbProcedureStatement(connection, request))
                .isInstanceOf(MAATApplicationException.class)
                .hasMessage("Invalid procedure name");
    }
}
