package gov.uk.courtdata.authorization.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import gov.uk.courtdata.authorization.service.AuthorizationService;
import gov.uk.courtdata.dto.ErrorDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authorization")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Authorization", description = "Rest API for performing role authorization checks")
public class AuthorizationController {

    private final Gson gson;
    private final AuthorizationService authorizationService;

    @GetMapping(value = "/users/{username}/validation/action/{action}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Check role action privileges")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<Object> isRoleActionAuthorized(@PathVariable String username, @PathVariable String action) {
        log.info("Check-Role-Action Request Received");
        boolean isAuthorized = authorizationService.isRoleActionAuthorized(username, action);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("result", Boolean.toString(isAuthorized));
        return ResponseEntity.ok(gson.toJson(jsonObject));
    }
}
