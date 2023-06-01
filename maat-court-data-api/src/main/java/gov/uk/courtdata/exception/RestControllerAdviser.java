package gov.uk.courtdata.exception;


import gov.uk.courtdata.constants.ErrorCodes;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.eform.exception.UsnException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

/**
 * <code>RestControllerAdviser</code> centralizes all rest controller exceptions.
 */
@Slf4j
@ControllerAdvice
public class RestControllerAdviser extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                            HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().body(ErrorDTO.builder()
                .code(HttpStatus.BAD_REQUEST.name())
                .message(ex.getMessage())
                .build());
    }

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
        log.warn(ex.getMessage());
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
        log.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorDTO.builder()
                .code(HttpStatus.NOT_FOUND.name())
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String errorMessage = String.format(
                "The provided value '%s' is the incorrect type for the '%s' parameter.", ex.getValue(), ex.getName());
        log.warn(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDTO.builder()
                .code(HttpStatus.BAD_REQUEST.name())
                .message(errorMessage)
                .build());
    }

    @ExceptionHandler(UsnException.class)
    public ResponseEntity<ErrorDTO> handleUsnValidationException(UsnException ex) {
        String errorMessage = ex.getMessage();
        log.warn(errorMessage);
        HttpStatus httpStatus = ex.getHttpResponseCode();
        
        return ResponseEntity.status(httpStatus).body(ErrorDTO.builder()
                .code(httpStatus.name())
                .message(errorMessage)
                .build());
    }

}
