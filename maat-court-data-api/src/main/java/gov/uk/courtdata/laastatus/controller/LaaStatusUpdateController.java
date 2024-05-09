package gov.uk.courtdata.laastatus.controller;

import com.google.gson.Gson;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.laastatus.service.LaaStatusServiceUpdate;
import gov.uk.courtdata.laastatus.validator.LaaStatusValidationProcessor;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.LaaTransactionLogging;
import gov.uk.courtdata.model.MessageCollection;
import gov.uk.courtdata.service.QueueMessageLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "LAA Status", description = "Rest APIs for LAA Status.")
@RequestMapping("maatApi")
public class LaaStatusUpdateController {

    private final LaaStatusValidationProcessor laaStatusValidationProcessor;
    private final Gson gson;
    private final QueueMessageLogService queueMessageLogService;


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
        UUID laaTransactionIdUUID = Optional.ofNullable(laaTransactionId).isPresent() ?
            UUID.fromString(laaTransactionId) :
            UUID.randomUUID();

        setupMDC(jsonPayload, laaTransactionIdUUID);
        log.info("LAA Status Update Request received.");

        queueMessageLogService.createLog(MessageType.LAA_STATUS_REST_CALL, jsonPayload);
        MessageCollection messageCollection;
        try {
            CaseDetails caseDetails = gson.fromJson(jsonPayload, CaseDetails.class);
            caseDetails.setLaaTransactionId(laaTransactionIdUUID);

            messageCollection = laaStatusValidationProcessor.validate(caseDetails);

            if (messageCollection.getMessages().isEmpty()) {
                log.info("Request Validation completed successfully");
                laaStatusServiceUpdate.updateMlaAndCDA(caseDetails);
            } else {
                log.info("LAA Status Update Validation Failed - Messages {}", messageCollection.getMessages());
            }
        } catch (Exception e) {
            String message = "MAAT API Call failed - %s".formatted(e.getMessage());
            log.error("An exception was thrown with message [{}]", e.getMessage(), e);
            throw new MAATCourtDataException(message);
        }
        return messageCollection;
    }

    private void setupMDC(String jsonPayload, UUID laaTransactionIdUUID) {
        LaaTransactionLogging laaTransactionLogging = gson.fromJson(jsonPayload,
            LaaTransactionLogging.class);

        LoggingData.MAATID.putInMDC(laaTransactionLogging.getMaatId());
        LoggingData.LAA_TRANSACTION_ID.putInMDC(laaTransactionIdUUID);
        LoggingData.CASE_URN.putInMDC(laaTransactionLogging.getCaseUrn());
    }
}
