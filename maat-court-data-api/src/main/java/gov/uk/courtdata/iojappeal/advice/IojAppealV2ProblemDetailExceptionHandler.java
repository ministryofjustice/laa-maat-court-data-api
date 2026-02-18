package gov.uk.courtdata.iojappeal.advice;

import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.iojappeal.controller.IOJAppealControllerV2;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import uk.gov.justice.laa.crime.exception.ValidationException;
import uk.gov.justice.laa.crime.util.ProblemDetailUtil;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(assignableTypes = IOJAppealControllerV2.class)
public class IojAppealV2ProblemDetailExceptionHandler {


    @ExceptionHandler(RequestedObjectNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatchException(RequestedObjectNotFoundException ex) {

    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String errorMessage = String.format(
                "The provided value '%s' is the incorrect type for the '%s' parameter.", ex.getValue(), ex.getName());
        log.warn(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDTO.builder()
                .code(HttpStatus.BAD_REQUEST.name())
                .message(errorMessage)
                .build());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleValidation(DataIntegrityViolationException ex) {

    }

    @ExceptionHandler(CrimeValidationException.class)
    public ResponseEntity<ProblemDetail> handleValidation(ValidationException ex) {
        // choose your status (400 is typical for validation)
        ProblemDetail pd = ProblemDetailUtil.buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                /* your extension object here */
        );
        return ResponseEntity.status(pd.getStatus()).body(pd);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnhandled(Exception ex) {
        // avoid leaking internals; log full exception, return generic detail
        ProblemDetail pd = problemDetailUtil.buildProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected error",
                /* extension incl traceId etc */
        );
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
        ErrorExtension extension = buildErrorExtension(errorCode, "", List.of());
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

