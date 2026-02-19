package gov.uk.courtdata.iojappeal.advice;

import gov.uk.courtdata.constants.ErrorCodes;
import gov.uk.courtdata.exception.CrimeValidationException;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.iojappeal.controller.IOJAppealControllerV2;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import uk.gov.justice.laa.crime.error.ErrorExtension;
import uk.gov.justice.laa.crime.error.ErrorMessage;
import uk.gov.justice.laa.crime.util.ProblemDetailUtil;

@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = IOJAppealControllerV2.class)
public class IojAppealV2ProblemDetailExceptionHandler {

    @ExceptionHandler(RequestedObjectNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatchException(RequestedObjectNotFoundException ex) {
        return buildSimpleErrorResponse(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.name(), ex.getMessage());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatchException(Exception ex) {
        return buildSimpleErrorResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.name(), ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleValidation(DataIntegrityViolationException ex) {
        return buildSimpleErrorResponse(HttpStatus.NOT_FOUND, ErrorCodes.DB_ERROR, ex.getMessage());
    }

    @ExceptionHandler(CrimeValidationException.class)
    public ResponseEntity<ProblemDetail> handleValidation(CrimeValidationException ex) {
        ErrorExtension extension = buildErrorExtension(
            "VALIDATION_FAILURE", MDC.get("traceId"), ex.getExceptionMessages());
        return buildSimpleErrorResponse(HttpStatus.BAD_REQUEST, "Validation Failure", extension);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnhandled(Exception ex) {
        // avoid leaking internals; log full exception, return generic detail
        ProblemDetail pd = ProblemDetailUtil.buildProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected error",
                buildErrorExtension(HttpStatus.INTERNAL_SERVER_ERROR.name(), MDC.get("traceId"), List.of()
        ));
        logError(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ex.getMessage());
        return ResponseEntity.status(pd.getStatus()).body(pd);
    }

    private ResponseEntity<ProblemDetail> buildSimpleErrorResponse(
            HttpStatusCode status, String message, ErrorExtension extension) {
        logError(status.toString(), message);
        return new ResponseEntity<>(ProblemDetailUtil.buildProblemDetail(status, message, extension), status);
    }

    private ResponseEntity<ProblemDetail> buildSimpleErrorResponse(
            HttpStatusCode status, String errorCode, String message) {
        logError(status.toString(), message);
        // create extension with empty list. Detail will suffice.
        ErrorExtension extension = buildErrorExtension(errorCode, MDC.get("traceId"), List.of());
        return buildSimpleErrorResponse(status, message, extension);
    }

    private ErrorExtension buildErrorExtension(String code, String traceId, List<ErrorMessage> errorMessages) {
        return ProblemDetailUtil.buildErrorExtension(code, traceId, errorMessages);
    }

    private void logError(String status, String message) {
        log.error(
                "Exception Occurred. Status - {}, Detail - {}, TraceId - {}",
                status,
                message,
                "");
    }
}

