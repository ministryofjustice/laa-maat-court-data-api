package gov.uk.courtdata.exception;


import gov.uk.courtdata.constants.ErrorCodes;
import gov.uk.courtdata.dto.ErrorDTO;
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
        log.error(ex.getMessage());
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
                .code(ErrorCodes.OBJECT_NOT_FOUND)
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("Data integrity violation. Error: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ErrorDTO.builder()
                .code(ErrorCodes.DB_ERROR)
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorDTO> handleDataAccessException(DataAccessException ex) {
        log.error("Problem accessing the database. Error: {}", ex.getMessage());
        return ResponseEntity.internalServerError().body(ErrorDTO.builder()
                .code(ErrorCodes.DB_ERROR)
                .message(ex.getMessage())
                .build());
    }

    /**
     * Handles exceptions where an object is requested but is not found in the database
     */
    @ExceptionHandler(RequestedObjectNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleRequestedObjectNotFoundException(RequestedObjectNotFoundException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorDTO.builder()
                .code(HttpStatus.NOT_FOUND.name())
                .message(ex.getMessage())
                .build());
    }
}
