package gov.uk.courtdata.hearing.service;

import gov.uk.courtdata.exception.GlobalAppLoggingHandler;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;

@RunWith(MockitoJUnitRunner.class)
public class HearingResultedListenerAspectTest {

    @Mock
    private HearingResultedListener hearingResultedListener;

    private HearingResultedListener hearingResultedListenerProxy;

    @InjectMocks
    GlobalAppLoggingHandler globalAppLoggingHandler;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeEach
    public void setUp() {

        AspectJProxyFactory factory = new AspectJProxyFactory();
        factory.setTarget(hearingResultedListener);
        factory.addAspect(globalAppLoggingHandler);
        hearingResultedListenerProxy = factory.getProxy();
    }

    @Test
    public void givenJSONMessageIsReceived_whenHearingResultedListenerMAATIdNull() {

        thrown.expect(RuntimeException.class);

        //given
        String message = "{\"laaTransactionId\":\"c77c96ff-7cad-44cc-9e12-5bc80f5f2d9e\" ,\n" +
                "  \"caseUrn\":\"CASNUM-ABC123\",\n" +
                "  \"maatId\": \"null\"}";
        //when
        lenient().when(hearingResultedListener).getMock();
        doThrow(new RuntimeException()).when(hearingResultedListener).receive(message);

        //then
        hearingResultedListenerProxy.receive(message);
    }
}
