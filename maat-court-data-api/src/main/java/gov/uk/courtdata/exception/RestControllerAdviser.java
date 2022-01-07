package gov.uk.courtdata.exception;


import gov.uk.courtdata.dto.ErrorDTO;
import io.sentry.util.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * <code>RestControllerAdviser</code> centralizes all rest controller exceptions.
 */
@Slf4j
@ControllerAdvice
public class RestControllerAdviser extends ResponseEntityExceptionHandler {

    /**
     * Redirect all runtime exceptions to internal handler.
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({MAATCourtDataException.class})
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {

        return handleExceptionInternal(ex,
                ErrorDTO.builder()
                        .message(ex.getMessage())
                        .build(),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Handles business validation exceptions.
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorDTO> handleValidationError(Exception ex) {

        return ResponseEntity.badRequest().body(ErrorDTO.builder()
                .code("")
                .message(ex.getMessage())
                .build());
    }
    /**
     * Handles business validation exceptions.
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorDTO> handleNoSuchElementException(Exception ex) {

        return ResponseEntity.badRequest().body(ErrorDTO.builder()
                .code("OBJECT NOT FOUND")
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDTO> handleDataIntegrityViolationException(Exception ex) {
        log.error("Problem with database. Error: {}", ex);
        return ResponseEntity.badRequest().body(ErrorDTO.builder()
                .code("DB Error")
                .message(ex.getMessage() + ". "+ ExceptionUtils.findRootCause(ex).getMessage())
                .build());
    }
}
