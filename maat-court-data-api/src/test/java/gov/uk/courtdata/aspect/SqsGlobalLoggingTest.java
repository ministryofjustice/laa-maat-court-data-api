package gov.uk.courtdata.aspect;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;

import ch.qos.logback.classic.Level;
import com.google.gson.Gson;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.hearing.service.HearingResultedListener;
import gov.uk.courtdata.hearing.service.HearingResultedService;
import gov.uk.courtdata.service.QueueMessageLogService;
import gov.uk.courtdata.testutils.LoggingMemoryAppender;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.messaging.MessageHeaders;

@ExtendWith(MockitoExtension.class)
class SqsGlobalLoggingTest {

    private LoggingMemoryAppender loggingMemoryAppender;

    @Mock
    private HearingResultedService mockHearingResultedService;

    @Mock
    private QueueMessageLogService mockQueueMessageLogService;

    private HearingResultedListener hearingResultedListenerProxy;

    @BeforeEach
    void setUp() {
        loggingMemoryAppender = new LoggingMemoryAppender();
        loggingMemoryAppender.addAppenderTo(SqsGlobalLogging.class);
        loggingMemoryAppender.start();

        HearingResultedListener hearingResultedListener = new HearingResultedListener(new Gson(),
                mockHearingResultedService, mockQueueMessageLogService);

        AspectJProxyFactory factory = new AspectJProxyFactory(hearingResultedListener);

        SqsGlobalLogging sqsGlobalLogging = new SqsGlobalLogging(new Gson());
        factory.addAspect(sqsGlobalLogging);

        hearingResultedListenerProxy = factory.getProxy();
    }

    @AfterEach
    void cleanUp() {
        loggingMemoryAppender.reset();
        loggingMemoryAppender.stop();
    }

    @Test
    void givenJsonMessageIsReceivedWithNullMaatId_shouldLogViaAspects() {
        //given
        final String message = """
                {
                  "maatId": null,
                  "caseUrn": "CASNUM-ABC123",
                  "metadata": {
                       "laaTransactionId": "c77c96ff-7cad-44cc-9e12-5bc80f5f2d9e"
                    }
                }
                """;
        final MessageHeaders headers = new MessageHeaders(new HashMap<>());
        RuntimeException expectedRuntimeException = new RuntimeException(
                "Error processing message");
        doThrow(expectedRuntimeException).when(mockQueueMessageLogService)
                .createLog(MessageType.HEARING, message);

        //when
        RuntimeException actualRuntimeException = Assertions.assertThrows(RuntimeException.class,
                () -> hearingResultedListenerProxy.receive(message, headers));

        //then
        assertAll(
                () -> assertThat(actualRuntimeException.getMessage())
                        .isEqualTo("Error processing message"),

                () -> loggingMemoryAppender.assertContains(
                        "Received JSON payload from the queue", Level.INFO),

                () -> loggingMemoryAppender.assertContains(
                        "Exception thrown when processing message: Error processing message",
                        Level.ERROR),

                () -> loggingMemoryAppender.assertContains(
                        "Message processing completed", Level.INFO),

                () -> loggingMemoryAppender.assertContains(
                        "About to clear down the MDC", Level.DEBUG)
        );

    }

    @Test
    void givenPopulatedJsonMessageIsReceived_MdcShouldBePopulatedAndClearedDown() {
        //given
        final String message = """
                {
                  "maatId": 6184652,
                  "caseUrn": "CASNUM-ABC123",
                  "metadata": {
                       "laaTransactionId": "c77c96ff-7cad-44cc-9e12-5bc80f5f2d9e"
                    }
                }
                """;
        final MessageHeaders headers = new MessageHeaders(new HashMap<>());
        final RuntimeException expectedRuntimeException = new RuntimeException(
                "Error processing message");

        MDC.clear();
        final Map<String, String> actualMdcContextMap = new HashMap<>();

        doAnswer(invocationOnMock -> {
            actualMdcContextMap.putAll(MDC.getCopyOfContextMap());
            throw expectedRuntimeException;
        }).when(mockQueueMessageLogService).createLog(MessageType.HEARING, message);

        //when
        RuntimeException actualRuntimeException = Assertions.assertThrows(RuntimeException.class,
                () -> hearingResultedListenerProxy.receive(message, headers));

        //then
        assertAll(
                () -> assertThat(actualRuntimeException.getMessage())
                        .isEqualTo("Error processing message"),

                // Assert that the MDC has been cleared
                () -> loggingMemoryAppender.assertContains(
                        "About to clear down the MDC", Level.DEBUG),
                () -> assertThat(MDC.getCopyOfContextMap()).isNull(),

                // Assert that the MDC was previously populated
                () -> assertThat(actualMdcContextMap)
                        .containsEntry("laaTransactionId", "c77c96ff-7cad-44cc-9e12-5bc80f5f2d9e"),
                () -> assertThat(actualMdcContextMap)
                        .containsEntry("caseUrn", "CASNUM-ABC123"),
                () -> assertThat(actualMdcContextMap)
                        .containsEntry("requestType", "HEARING"),
                () -> assertThat(actualMdcContextMap)
                        .containsEntry("maatId", "6184652")
        );

    }
}
