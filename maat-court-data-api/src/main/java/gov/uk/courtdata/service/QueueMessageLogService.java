package gov.uk.courtdata.service;

import static gov.uk.courtdata.enums.MessageType.LAA_STATUS_UPDATE;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gov.uk.courtdata.entity.QueueMessageLogEntity;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.repository.QueueMessageLogRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueMessageLogService {

    private final QueueMessageLogRepository queueMessageLogRepository;

    public int getMessageCounterByMaatId(Integer maatId) {

        return queueMessageLogRepository.getMessageCounterByMaatId(maatId);
    }


    public void createLog(final MessageType messageType, final String message) {

        JsonObject msgObject = JsonParser.parseString(message).getAsJsonObject();

        JsonElement maatId = messageType.equals(LAA_STATUS_UPDATE) ?
                msgObject.get("data")
                        .getAsJsonObject().get("attributes")
                        .getAsJsonObject().get("maat_reference")
                : msgObject.get("maatId");

        String laaTransactionId = extractLaaTransactionId(msgObject);

        QueueMessageLogEntity queueMessageLogEntity =
                QueueMessageLogEntity.builder()
                        .transactionUUID(UUID.randomUUID().toString())
                        .laaTransactionId(laaTransactionId)
                        .maatId(Optional
                                .ofNullable(maatId)
                                .map(JsonElement::getAsInt)
                                .orElse(-1))
                        .type(prepareMessageType(messageType, msgObject))
                        .message(convertAsByte(message))
                        .createdTime(LocalDateTime.now())
                        .build();

        queueMessageLogRepository.save(queueMessageLogEntity);
    }

    private String extractLaaTransactionId(JsonObject msgObject) {
        JsonElement laaTransactionUUID = msgObject.has("metadata") ?
                msgObject.get("metadata").getAsJsonObject().get("laaTransactionId") :
                msgObject.get("laaTransactionId");

        if (laaTransactionUUID == null || laaTransactionUUID.isJsonNull()) {
            return null;
        }

        return laaTransactionUUID.getAsString();
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
