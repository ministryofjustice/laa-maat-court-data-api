package gov.uk.courtdata.annotation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.http.MediaType;
import uk.gov.justice.laa.crime.error.ProblemDetailError;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", 
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
    @ApiResponse(responseCode = "400", description = "Bad Request.", 
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
        schema = @Schema(implementation = ProblemDetailError.class))),
    @ApiResponse(responseCode = "404", description = "Not Found.",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ProblemDetailError.class))),
    @ApiResponse(responseCode = "500", description = "Server Error.", 
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
        schema = @Schema(implementation = ProblemDetailError.class))),
})
public @interface StandardProblemDetailErrorResponse {

}
