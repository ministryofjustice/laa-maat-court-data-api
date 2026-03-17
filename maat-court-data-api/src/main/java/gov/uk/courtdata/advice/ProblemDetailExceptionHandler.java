package gov.uk.courtdata.advice;

import gov.uk.courtdata.exception.CrimeValidationException;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.iojappeal.controller.IOJAppealControllerV2;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import uk.gov.justice.laa.crime.tracing.TraceIdHandler;
import uk.gov.justice.laa.crime.error.ErrorExtension;
import uk.gov.justice.laa.crime.error.ErrorMessage;
import uk.gov.justice.laa.crime.util.ProblemDetailUtil;

@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = IOJAppealControllerV2.class)
public class ProblemDetailExceptionHandler {

    private final ObjectProvider<TraceIdHandler> traceIdHandlerProvider;

    private String getTraceId() {
        return Optional.ofNullable(traceIdHandlerProvider.getIfAvailable())
                .map(TraceIdHandler::getTraceId)
                .orElse("");
    }

    @ExceptionHandler(RequestedObjectNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(RequestedObjectNotFoundException ex) {
        log.info("Resource not found. TraceId={} Detail={}", getTraceId(), ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ProblemDetailError.OBJECT_NOT_FOUND,
                ex.getMessage(), List.of());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class})
    public ResponseEntity<ProblemDetail> handleBadRequest(Exception ex) {
        log.warn("Bad request. TraceId={} Detail={}", getTraceId(), ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ProblemDetailError.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ProblemDetail> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        log.warn("Method not supported. TraceId={} Detail={}", getTraceId(), ex.getMessage());
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, ProblemDetailError.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ProblemDetail> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        log.warn("Unsupported media type. TraceId={} Detail={}", getTraceId(), ex.getMessage());
        return buildResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ProblemDetailError.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {
        log.warn("DB constraint violation. TraceId={}", getTraceId(), ex);
        return buildResponse(HttpStatus.BAD_REQUEST, ProblemDetailError.DB_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {

        log.warn("Bean validation failed. TraceId={} Detail={}", getTraceId(),
                ex.getMessage());
        var messages = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> new ErrorMessage(e.getField(), e.getDefaultMessage()))
                .toList();

        return buildResponse(HttpStatus.BAD_REQUEST, ProblemDetailError.VALIDATION_FAILURE,
                messages);
    }

    @ExceptionHandler(CrimeValidationException.class)
    public ResponseEntity<ProblemDetail> handleValidationFailure(CrimeValidationException ex) {
        log.warn("Crime validation exception. TraceId={} Errors={} Detail={}",
                getTraceId(),
                ex.getExceptionMessages().size(), String.join(", ",
                        ex.getExceptionMessages().stream().map(ErrorMessage::message).toList()));
        return buildResponse(HttpStatus.BAD_REQUEST, ProblemDetailError.VALIDATION_FAILURE,
                ex.getExceptionMessages());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnhandled(Exception ex) {
        log.error("Unhandled exception. TraceId={}", getTraceId(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                ProblemDetailError.APPLICATION_ERROR);
    }

    private ErrorExtension buildErrorExtension(String code, String traceId,
            List<ErrorMessage> errorMessages) {
        return ProblemDetailUtil.buildErrorExtension(code, traceId, errorMessages);
    }

    private ResponseEntity<ProblemDetail> buildResponse(
            HttpStatusCode status, ProblemDetailError error, String detailOverride,
            List<ErrorMessage> errors) {

        ErrorExtension extension = buildErrorExtension(error.code(), getTraceId(),
                errors);
        return ResponseEntity.status(status)
                .body(ProblemDetailUtil.buildProblemDetail(status, detailOverride, extension));
    }

    private ResponseEntity<ProblemDetail> buildResponse(
            HttpStatusCode status, ProblemDetailError error) {

        return buildResponse(status, error, error.defaultDetail(), List.of());
    }

    private ResponseEntity<ProblemDetail> buildResponse(
            HttpStatusCode status, ProblemDetailError error, List<ErrorMessage> errors) {

        return buildResponse(status, error, error.defaultDetail(), errors);
    }

}

