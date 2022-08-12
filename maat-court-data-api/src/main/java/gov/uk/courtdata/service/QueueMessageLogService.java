package gov.uk.courtdata.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gov.uk.courtdata.entity.QueueMessageLogEntity;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.repository.QueueMessageLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static gov.uk.courtdata.enums.MessageType.*;

@Service
@Slf4j
@XRayEnabled
@RequiredArgsConstructor
public class QueueMessageLogService {

    private final QueueMessageLogRepository queueMessageLogRepository;

    public int getMessageCounterByMaatId(Integer maatId) {

        return queueMessageLogRepository.getMessageCounterByMaatId(maatId);
    }


    public void createLog(final MessageType messageType, final String message) {

        JsonObject msgObject = JsonParser.parseString(message).getAsJsonObject();

        String laaTransactionIdStr = null;
        JsonElement maatId;

        if (messageType.equals(LAA_STATUS_UPDATE) ) {

            maatId = msgObject.get("data")
                    .getAsJsonObject().get("attributes")
                    .getAsJsonObject().get("maat_reference");

        } else if (messageType.equals(LAA_STATUS_REST_CALL)) {

            laaTransactionIdStr = msgObject.get("laaTransactionId").getAsString();
            maatId = msgObject.get("maatId");

        } else if (messageType.equals(LINK) || messageType.equals(UNLINK)) {
            maatId = msgObject.get("maatId");
            laaTransactionIdStr = msgObject.get("laaTransactionId").getAsString();

        } else {

            maatId = msgObject.get("maatId");
            if (msgObject.has("metadata") ){

                JsonObject metadata = msgObject.get("metadata").getAsJsonObject();
                laaTransactionIdStr =  metadata.has("laaTransactionId") ? metadata.get("laaTransactionId").getAsString() : null;

            }
        }



        QueueMessageLogEntity queueMessageLogEntity =
                QueueMessageLogEntity.builder()
                        .transactionUUID(UUID.randomUUID().toString())
                        .laaTransactionId(laaTransactionIdStr)
                        .maatId(Optional
                                .ofNullable(maatId)
                                .map(JsonElement::getAsInt)
                                .orElse(-1) )
                        .type(prepareMessageType(messageType, msgObject))
                        .message(convertAsByte(message))
                        .createdTime(LocalDateTime.now())
                        .build();

        queueMessageLogRepository.save(queueMessageLogEntity);
    }


    private String prepareMessageType(MessageType messageType, JsonObject msgObject) {

        final JsonElement jurType = msgObject.get("jurisdictionType");
        final StringBuilder msgBuilder = new StringBuilder().append(messageType.name());

        Optional<String> jurisdiction = Optional.ofNullable(jurType).map(JsonElement::getAsString);

        if (jurisdiction.isPresent()) {
            msgBuilder
                    .append("-")
                    .append(jurisdiction.get());
        }

        return msgBuilder.toString();
    }


    private byte[] convertAsByte(final String message) {

        return Optional.ofNullable(message).isPresent() ?
                message.getBytes() : null;
    }
}
