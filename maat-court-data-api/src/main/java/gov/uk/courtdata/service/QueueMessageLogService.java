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

        JsonElement maatId = messageType.equals(LAA_STATUS_UPDATE) ?
                msgObject.get("data")
                        .getAsJsonObject().get("attributes")
                        .getAsJsonObject().get("maat_reference")
                : msgObject.get("maatId");

        JsonElement uuid = msgObject.has("metadata") ?
                msgObject.get("metadata").getAsJsonObject().get("laaTransactionId") :
                msgObject.get("laaTransactionId");

        QueueMessageLogEntity queueMessageLogEntity =
                QueueMessageLogEntity.builder()
                        .transactionUUID(UUID.randomUUID().toString())
                        .laaTransactionId(Optional.ofNullable(uuid).map(JsonElement::getAsString)
                                .orElse(null))
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
