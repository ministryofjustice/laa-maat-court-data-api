package gov.uk.courtdata.common.annotations;

import gov.uk.courtdata.dto.ErrorDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@ApiResponse(
        responseCode = "404",
        description = "Not Found.",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorDTO.class)))
@MaatApiWriteErrorResponses
public @interface MaatApiReadErrorResponses {
}
