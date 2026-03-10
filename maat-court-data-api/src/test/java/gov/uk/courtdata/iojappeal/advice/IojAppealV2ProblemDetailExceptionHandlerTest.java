package gov.uk.courtdata.iojappeal.advice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.exception.CrimeValidationException;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import uk.gov.justice.laa.crime.error.ErrorExtension;
import uk.gov.justice.laa.crime.error.ErrorMessage;
import uk.gov.justice.laa.crime.tracing.TraceIdHandler;
import uk.gov.justice.laa.crime.util.ProblemDetailUtil;

class IojAppealV2ProblemDetailExceptionHandlerTest {

    private static final String TEST_TRACE_ID = "abc-123";

    private final TraceIdHandler traceIdHandler = mock(TraceIdHandler.class);

    @SuppressWarnings("unchecked")
    private final ObjectProvider<TraceIdHandler> traceIdHandlerProvider = mock(ObjectProvider.class);

    private final IojAppealV2ProblemDetailExceptionHandler handler =
            new IojAppealV2ProblemDetailExceptionHandler(traceIdHandlerProvider);

    @BeforeEach
    void setUp() {
        when(traceIdHandler.getTraceId()).thenReturn(TEST_TRACE_ID);
        when(traceIdHandlerProvider.getIfAvailable()).thenReturn(traceIdHandler);
    }

    @Test
    void handleNotFound_returns404AndUsesExceptionMessageAsDetail() {
        RequestedObjectNotFoundException ex =
                new RequestedObjectNotFoundException("IOJ appeal not found");

        var response = handler.handleNotFound(ex);

        assertProblemDetail(
                response,
                HttpStatus.NOT_FOUND,
                "IOJ appeal not found",
                ProblemDetailError.OBJECT_NOT_FOUND.code(),
                List.of()
        );
    }

    @Test
    void handleBadRequest_forMethodArgumentTypeMismatch_returns400() {
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getMessage()).thenReturn("Type mismatch");

        var response = handler.handleBadRequest(ex);

        assertProblemDetail(
                response,
                HttpStatus.BAD_REQUEST,
                ProblemDetailError.BAD_REQUEST.defaultDetail(),
                ProblemDetailError.BAD_REQUEST.code(),
                List.of()
        );
    }

    @Test
    void handleBadRequest_forHttpMessageNotReadable_returns400() {
        HttpMessageNotReadableException ex =
                new HttpMessageNotReadableException("Malformed JSON");

        var response = handler.handleBadRequest(ex);

        assertProblemDetail(
                response,
                HttpStatus.BAD_REQUEST,
                ProblemDetailError.BAD_REQUEST.defaultDetail(),
                ProblemDetailError.BAD_REQUEST.code(),
                List.of()
        );
    }

    @Test
    void handleDataIntegrityViolation_returns400() {
        DataIntegrityViolationException ex =
                new DataIntegrityViolationException("Constraint violation");

        var response = handler.handleDataIntegrityViolation(ex);

        assertProblemDetail(
                response,
                HttpStatus.BAD_REQUEST,
                ProblemDetailError.DB_ERROR.defaultDetail(),
                ProblemDetailError.DB_ERROR.code(),
                List.of()
        );
    }

    @Test
    void handleMethodArgumentNotValidException_returns400AndValidationFailureDetail() {
        FieldError fieldError = new FieldError(
                "request",
                "iojReason",
                "Cannot be null.");

        List<ErrorMessage> expectedErrors = List.of(
                new ErrorMessage("iojReason", "Cannot be null.")
        );

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(ex.getMessage()).thenReturn("Validation failed");

        var response = handler.handleMethodArgumentNotValidException(ex);

        assertProblemDetail(
                response,
                HttpStatus.BAD_REQUEST,
                ProblemDetailError.VALIDATION_FAILURE.defaultDetail(),
                ProblemDetailError.VALIDATION_FAILURE.code(),
                expectedErrors
        );
    }

    @Test
    void handleValidationFailure_returns400AndValidationFailureDetail() {
        List<ErrorMessage> expectedErrors = List.of(
                new ErrorMessage("iojReason", "Appeal Reason is invalid."),
                new ErrorMessage("reasonForAppeal", "reasonForAppeal is missing.")
        );

        CrimeValidationException ex = new CrimeValidationException(expectedErrors);

        var response = handler.handleValidationFailure(ex);

        assertProblemDetail(
                response,
                HttpStatus.BAD_REQUEST,
                ProblemDetailError.VALIDATION_FAILURE.defaultDetail(),
                ProblemDetailError.VALIDATION_FAILURE.code(),
                expectedErrors
        );
    }

    @Test
    void handleUnhandled_returns500() {
        RuntimeException ex = new RuntimeException("Error");

        var response = handler.handleUnhandled(ex);

        assertProblemDetail(
                response,
                HttpStatus.INTERNAL_SERVER_ERROR,
                ProblemDetailError.APPLICATION_ERROR.defaultDetail(),
                ProblemDetailError.APPLICATION_ERROR.code(),
                List.of()
        );
    }

    private void assertProblemDetail(
            ResponseEntity<ProblemDetail> response,
            HttpStatus expectedStatus,
            String expectedDetail,
            String expectedCode,
            List<ErrorMessage> expectedErrors) {

        ProblemDetail body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.getDetail()).isEqualTo(expectedDetail);
        assertThat(response.getStatusCode()).isEqualTo(expectedStatus);

        ErrorExtension errorExtension = getErrorExtension(body);

        assertThat(errorExtension.traceId()).isEqualTo(TEST_TRACE_ID);
        assertThat(errorExtension.code()).isEqualTo(expectedCode);
        assertThat(errorExtension.errors()).containsExactlyInAnyOrderElementsOf(expectedErrors);
    }

    @Test
    void handleUnhandled_whenTraceIdHandlerIsUnavailable_returnsEmptyTraceId() {
        when(traceIdHandlerProvider.getIfAvailable()).thenReturn(null);

        var response = handler.handleUnhandled(new RuntimeException("Error"));

        ErrorExtension errorExtension = getErrorExtension(response.getBody());

        assertThat(errorExtension.traceId()).isEmpty();
        assertThat(errorExtension.code()).isEqualTo(ProblemDetailError.APPLICATION_ERROR.code());
    }

    private ErrorExtension getErrorExtension(ProblemDetail body) {
        return ProblemDetailUtil.getErrorExtension(body)
                .orElseThrow(() -> new AssertionError("Expected error extension to be present"));
    }
}
