package gov.uk.courtdata.laastatus.controller;

import com.google.gson.Gson;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.laastatus.service.LaaStatusServiceUpdate;
import gov.uk.courtdata.laastatus.validator.LaaStatusValidationProcessor;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.MessageCollection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;



@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("maatApi")
@Tag(name = "LAA Status", description = "Rest APIs for LAA Status.")
public class LaaStatusUpdateController {

    private final LaaStatusValidationProcessor laaStatusValidationProcessor;
    private final Gson gson;

    private final LaaStatusServiceUpdate laaStatusServiceUpdate;

    @PostMapping("/laaStatus")
    @Operation(summary = "Process LAA Status updates.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageCollection.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })

    public MessageCollection updateLAAStatus(@RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId,
                                             @Parameter(description = "Case details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CaseDetails.class)))
                                             @RequestBody String jsonPayload) {

        UUID laaTransactionIdUUID = Optional.ofNullable(laaTransactionId).isPresent() ? UUID.fromString(laaTransactionId) : UUID.randomUUID();
        MDC.put(LoggingData.LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info("LAA Status Update Request received.");
        MessageCollection messageCollection;
        try {
            CaseDetails caseDetails = gson.fromJson(jsonPayload, CaseDetails.class);
            caseDetails.setLaaTransactionId(laaTransactionIdUUID);

            messageCollection = laaStatusValidationProcessor.validate(caseDetails);

            if (messageCollection.getMessages().isEmpty()) {
                log.info("Request Validation is successfully completed.");
                laaStatusServiceUpdate.updateMlaAndCDA(caseDetails);

            } else {
                log.info("LAA Status Update Validation Failed - Messages {}", messageCollection.getMessages());
            }
        } catch (Exception exception) {
            throw new MAATCourtDataException("MAAT APT Call failed " + exception.getMessage());
        }
        return messageCollection;
    }
}
