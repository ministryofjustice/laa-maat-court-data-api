package gov.uk.courtdata.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@ControllerAdvice
@Slf4j
public class RestControllerExceptionHandler {

    @ExceptionHandler(MaatCourtDataException.class)
    public void handleMaatCourtDataException(Exception ex) {

        log.warn("");

    }

    @ExceptionHandler(ValidationException.class)
    public void handleValidationException(Exception ex) {
        log.warn("validation failed");

    }


}
