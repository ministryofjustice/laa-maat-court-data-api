package gov.uk.courtdata.exception;


import gov.uk.courtdata.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        List<ErrorDTO> errors = Stream.of(ErrorDTO.builder()
                .message(ex.getMessage())
                .build()).collect(Collectors.toList());
        log.info("inside maat exception handler");
        return handleExceptionInternal(ex, errors,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Handles business validation exceptions.
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<List<ErrorDTO>> handleValidationError(Exception ex) {

        List<ErrorDTO> errors = Stream.of(ErrorDTO.builder()
                .code("error-1")
                .message(ex.getMessage())
                .build()).collect(Collectors.toList());

        log.info("inside validation handler");

        return ResponseEntity.badRequest().body(errors);
    }
}
