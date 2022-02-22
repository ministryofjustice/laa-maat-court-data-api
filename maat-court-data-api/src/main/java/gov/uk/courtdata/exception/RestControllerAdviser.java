package gov.uk.courtdata.exception;


import gov.uk.courtdata.dto.ErrorDTO;
import io.sentry.util.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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
        log.error("Error when handling request. Error: {}", ex);
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
    public ResponseEntity<ErrorDTO> handleValidationError(ValidationException ex) {
        log.error("Given input is not valid. Error: {}", ex);
        return ResponseEntity.badRequest().body(ErrorDTO.builder()
                .code(HttpStatus.BAD_REQUEST.name())
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
    public ResponseEntity<ErrorDTO> handleNoSuchElementException(NoSuchElementException ex) {
        log.error("Unable to find element for given input. Error: {}", ex);
        return ResponseEntity.badRequest().body(ErrorDTO.builder()
                .code("OBJECT NOT FOUND")
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("Problem when trying to insert/update into the database. Error: {}", ex);
        return ResponseEntity.badRequest().body(ErrorDTO.builder()
                .code("DB Error")
                .message(ex.getMessage() + ". "+ ExceptionUtils.findRootCause(ex).getMessage())
                .build());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorDTO> handleDataAccessException(DataAccessException ex) {
        log.error("Problem when accessing the database. Error: {}", ex);
        return ResponseEntity.internalServerError().body(ErrorDTO.builder()
                .code("DB Error")
                .message(ex.getMessage() + ". "+ ExceptionUtils.findRootCause(ex).getMessage())
                .build());
    }

    /**
     * Handles exceptions where an object is requested but is not found in the database
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(RequestedObjectNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleRequestedObjectNotFoundException(RequestedObjectNotFoundException ex) {
        log.error("Object cannot be found for the given input. Error: {}", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorDTO.builder()
                .code("OBJECT NOT FOUND")
                .message(ex.getMessage())
                .build());
    }
}
