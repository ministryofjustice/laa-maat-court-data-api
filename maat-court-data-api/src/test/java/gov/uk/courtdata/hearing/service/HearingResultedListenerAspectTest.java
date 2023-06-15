package gov.uk.courtdata.hearing.service;

import gov.uk.courtdata.exception.GlobalAppLoggingHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.messaging.MessageHeaders;

import java.util.HashMap;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class HearingResultedListenerAspectTest {

    @Mock
    private HearingResultedListener hearingResultedListener;

    private HearingResultedListener hearingResultedListenerProxy;

    @InjectMocks
    GlobalAppLoggingHandler globalAppLoggingHandler;

    @BeforeEach
    public void setUp() {

        AspectJProxyFactory factory = new AspectJProxyFactory();
        factory.setTarget(hearingResultedListener);
        factory.addAspect(globalAppLoggingHandler);
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
