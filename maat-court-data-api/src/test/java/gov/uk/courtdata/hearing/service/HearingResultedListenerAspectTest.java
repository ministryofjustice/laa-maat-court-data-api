package gov.uk.courtdata.hearing.service;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;

import gov.uk.courtdata.exception.SqsGlobalLoggingHandler;
import java.util.HashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.messaging.MessageHeaders;

@ExtendWith(MockitoExtension.class)
public class HearingResultedListenerAspectTest {

    @Mock
    private HearingResultedListener hearingResultedListener;

    private HearingResultedListener hearingResultedListenerProxy;

    @InjectMocks
    SqsGlobalLoggingHandler sqsGlobalLoggingHandler;

    @BeforeEach
    public void setUp() {

        AspectJProxyFactory factory = new AspectJProxyFactory();
        factory.setTarget(hearingResultedListener);
        factory.addAspect(sqsGlobalLoggingHandler);
        hearingResultedListenerProxy = factory.getProxy();
    }

    @Test
    public void givenJSONMessageIsReceived_whenHearingResultedListenerMAATIdNull() {

        Assertions.assertThrows(RuntimeException.class, () -> {
            //given
            String message = "{\"laaTransactionId\":\"c77c96ff-7cad-44cc-9e12-5bc80f5f2d9e\" ,\n" +
                    "  \"caseUrn\":\"CASNUM-ABC123\",\n" +
                    "  \"maatId\": \"null\"}";
            //when
            lenient().when(hearingResultedListener).getMock();
            doThrow(new RuntimeException()).when(hearingResultedListener).receive(message, new MessageHeaders(new HashMap<>()));

            //then
            hearingResultedListenerProxy.receive(message, new MessageHeaders(new HashMap<>()));
        });
    }
}
