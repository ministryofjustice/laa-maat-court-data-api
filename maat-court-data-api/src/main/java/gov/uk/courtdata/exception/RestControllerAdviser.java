package gov.uk.courtdata.exception;

import gov.uk.courtdata.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestControllerAdviser {


    @ExceptionHandler({HttpMessageNotReadableException.class, MAATCourtDataException.class})
    public ResponseEntity<List<ErrorDTO>> handleBadRequest(Exception ex) {

        List<ErrorDTO> dtoList = new ArrayList<>();
        ErrorDTO.builder().code("error-1")
                .message("Please check whether request contains all request params.").build();
        return new ResponseEntity(dtoList,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<List<ErrorDTO>> handleValidationError(Exception ex) {

        List<ErrorDTO> dtoList = new ArrayList<>();
        ErrorDTO.builder().code("error-1")
                .message(ex.getMessage()).build();

        //todo could be a different error type
        return new ResponseEntity(dtoList,HttpStatus.BAD_REQUEST);
    }
}
