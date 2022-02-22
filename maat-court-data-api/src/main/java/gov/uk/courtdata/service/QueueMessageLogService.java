package gov.uk.courtdata.service;

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

import static gov.uk.courtdata.enums.MessageType.LAA_STATUS_UPDATE;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueMessageLogService {

    private final QueueMessageLogRepository queueMessageLogRepository;

    public int getMessageCounterByMaatId(Integer maatId) {

        return queueMessageLogRepository.getMessageCounterByMaatId(maatId);
    }


    public void createLog(final MessageType messageType, final String message) {

        JsonObject msgObject = JsonParser.parseString(message).getAsJsonObject();

        JsonElement uuid = msgObject.get("laaTransactionId");
        JsonElement maatId = messageType.equals(LAA_STATUS_UPDATE) ?
                msgObject.get("data")
                        .getAsJsonObject().get("attributes")
                        .getAsJsonObject().get("maat_reference")
                : msgObject.get("maatId");

        QueueMessageLogEntity queueMessageLogEntity =
                QueueMessageLogEntity.builder()
                        .transactionUUID(Optional.ofNullable(uuid).map(JsonElement::getAsString)
                                .orElseGet(UUID.randomUUID()::toString))
                        .maatId(
                                Optional.ofNullable(maatId).map(JsonElement::getAsInt).orElse(null)
                        )
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
