package gov.uk.courtdata.link.service;

import com.google.gson.Gson;
import gov.uk.courtdata.model.CaseDetails;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CreateLinkListenerTest {

    @InjectMocks
    private CreateLinkListener createLinkListener;
    @Mock
    private Gson gson;
    @Mock
    private CreateLinkService createLinkService;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenJSONMessageIsReceived_whenCreateLinkListenerIsInvoked_thenCreateLinkServiceIsCalled() {
        //given
        CaseDetails caseDetails = CaseDetails.builder().build();
        String message = "Test JSON";
        //when
        when(gson.fromJson(message, CaseDetails.class)).thenReturn(caseDetails);
        createLinkListener.receive(message);
        //then
        verify(createLinkService, times(1)).saveAndLink(caseDetails);
    }


}
