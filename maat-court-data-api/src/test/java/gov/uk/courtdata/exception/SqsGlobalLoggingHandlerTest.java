package gov.uk.courtdata.exception;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.google.gson.Gson;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.hearing.service.HearingResultedListener;
import gov.uk.courtdata.hearing.service.HearingResultedService;
import gov.uk.courtdata.service.QueueMessageLogService;
import java.util.HashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.messaging.MessageHeaders;

@ExtendWith(MockitoExtension.class)
public class SqsGlobalLoggingHandlerTest {

  @Mock
  private HearingResultedService mockHearingResultedService;

  @Mock
  private QueueMessageLogService mockQueueMessageLogService;

  @Mock
  private Logger mockLogger;

  private HearingResultedListener hearingResultedListenerProxy;

  @BeforeEach
  public void setUp() {
    HearingResultedListener hearingResultedListener = new HearingResultedListener(new Gson(),
        mockHearingResultedService,
        mockQueueMessageLogService);

    AspectJProxyFactory factory = new AspectJProxyFactory(hearingResultedListener);

    SqsGlobalLoggingHandler sqsGlobalLoggingHandler = new SqsGlobalLoggingHandler(new Gson()) {
      Logger log() {
        return mockLogger;
      }
    };
    factory.addAspect(sqsGlobalLoggingHandler);

    hearingResultedListenerProxy = factory.getProxy();
  }

  @Test
  public void givenJsonMessageIsReceivedWithNullMaatId_shouldLogViaAspects() {
    //given
    final String message = """
                        {
          "laaTransactionId": "c77c96ff-7cad-44cc-9e12-5bc80f5f2d9e",
          "caseUrn": "CASNUM-ABC123",
          "maatId": null
        }
                        """;
    final MessageHeaders headers = new MessageHeaders(new HashMap<>());
    RuntimeException expectedRuntimeException = new RuntimeException("Error processing message");
    doThrow(expectedRuntimeException)
        .when(mockQueueMessageLogService).createLog(MessageType.HEARING, message);

    //when
    RuntimeException actualRuntimeException = Assertions.assertThrows(
        RuntimeException.class, () -> hearingResultedListenerProxy.receive(message, headers));

    //then
    assertAll(
        () -> assertEquals("Error processing message", actualRuntimeException.getMessage()),
        () -> verify(mockLogger).info("Received a json payload from a queue and converted."),
        () -> verify(mockLogger).info("Exception thrown - {} ", "Error processing message"),
        () -> verify(mockLogger).error("Exception StackTrace",
            expectedRuntimeException),
        () -> verify(mockLogger).info("Message processing finished.")
    );

  }
}
