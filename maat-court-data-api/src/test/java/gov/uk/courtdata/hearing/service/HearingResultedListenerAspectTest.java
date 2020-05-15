package gov.uk.courtdata.hearing.service;

import com.google.gson.Gson;
import gov.uk.courtdata.exception.GlobalAppLoggingHandler;
import gov.uk.courtdata.model.hearing.HearingResulted;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class HearingResultedListenerAspectTest {

    @Mock
    private HearingResultedListener hearingResultedListener;

    private HearingResultedListener hearingResultedListenerProxy;

    @Mock
    private Gson gson;

    @Mock
    private HearingResultedService hearingResultedService;

    @InjectMocks
    GlobalAppLoggingHandler globalAppLoggingHandler = new GlobalAppLoggingHandler();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
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
        HearingResulted laaHearingDetails = HearingResulted.builder().build();
        String message = "{\"laaTransactionId\":\"c77c96ff-7cad-44cc-9e12-5bc80f5f2d9e\" ,\n" +
                "  \"caseUrn\":\"CASNUM-ABC123\",\n" +
                "  \"maatId\": \"0\"}";
        //when
        doThrow(new RuntimeException()).when(hearingResultedListener).receive(message);

        //then
        hearingResultedListenerProxy.receive(message);
    }
}
