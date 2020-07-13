package gov.uk.courtdata.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gov.uk.courtdata.entity.QueueMessageLogEntity;
import gov.uk.courtdata.enums.QueueMessageType;
import gov.uk.courtdata.repository.QueueMessageLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueMessageLogService {

    private final QueueMessageLogRepository queueMessageLogRepository;


    public void createLog(final QueueMessageType messageType, final String message) {

        JsonObject msgObject = JsonParser.parseString(message).getAsJsonObject();

        JsonElement uuid = msgObject.get("laaTransactionId");
        JsonElement maatId = msgObject.get("maatId");

        QueueMessageLogEntity queueMessageLogEntity =
                QueueMessageLogEntity.builder()
                        .transactionUUID(Optional.ofNullable(uuid).map(JsonElement::getAsString)
                                .orElseGet(UUID.randomUUID()::toString))
                        .maatId(maatId.getAsInt())
                        .type(prepareMessageType(messageType, msgObject))
                        .message(convertAsByte(message))
                        .createdTime(LocalDateTime.now())
                        .build();

        queueMessageLogRepository.save(queueMessageLogEntity);
    }


    private String prepareMessageType(QueueMessageType messageType, JsonObject msgObject) {

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
