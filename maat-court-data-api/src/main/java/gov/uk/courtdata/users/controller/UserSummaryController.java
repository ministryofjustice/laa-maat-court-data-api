package gov.uk.courtdata.users.controller;

import gov.uk.courtdata.annotation.NotFoundApiResponse;
import gov.uk.courtdata.annotation.StandardApiResponse;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.dto.UserSummaryDTO;
import gov.uk.courtdata.controller.StandardApiResponseCodes;
import gov.uk.courtdata.entity.UserEntity;
import gov.uk.courtdata.model.authorization.AuthorizationResponse;
import gov.uk.courtdata.users.service.UserSummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User Summary", description = "Rest API to manage user information")
@RequestMapping("${api-endpoints.user-domain}")
public class UserSummaryController {

    private final UserSummaryService userSummaryService;

    @GetMapping(value = "/summary/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get User Summary Information")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthorizationResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<UserSummaryDTO> getUserSummary(
            @PathVariable String username) {
        log.info("Get User Summary - request received");
        return ResponseEntity.ok(userSummaryService.getUserSummary(username));
    }

    @GetMapping(value = "/{username}")
    @Operation(description = "Retrieve a reservation")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @NotFoundApiResponse
    public UserEntity getUser(@PathVariable String username) {
        return userSummaryService.getUser(username);
    }

    @PostMapping("/")
    @Operation(description = "Create a user")
    @StandardApiResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    public ResponseEntity<Void> createUser(@RequestBody UserEntity userEntity) {
        userSummaryService.createUser(userEntity);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{username}")
    @Operation(description = "Patch user details that allow nullifying individual fields")
    @StandardApiResponseCodes
    @NotFoundApiResponse
    public ResponseEntity<Void> patchUser(@PathVariable String username,
                                          @RequestBody Map<String, Object> updateFields) {
        userSummaryService.patchUser(username, updateFields);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/")
    @Operation(description = "Update user details")
    @StandardApiResponseCodes
    @NotFoundApiResponse
    public ResponseEntity<Void> updateUser(@RequestBody UserEntity userEntity) {
        userSummaryService.updateUser(userEntity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{username}")
    @Operation(description = "Delete a User")
    @StandardApiResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userSummaryService.deleteUser(username);
        return ResponseEntity.ok().build();
    }
}
