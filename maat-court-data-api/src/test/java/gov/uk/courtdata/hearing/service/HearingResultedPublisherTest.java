package gov.uk.courtdata.hearing.service;

import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.hearing.validator.HearingValidationProcessor;
import gov.uk.courtdata.model.hearing.HearingResulted;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jms.core.JmsTemplate;

@RunWith(MockitoJUnitRunner.class)
public class HearingResultedPublisherTest {

    @InjectMocks
    private HearingResultedPublisher hearingResultedPublisher;

    @Mock
    private HearingValidationProcessor hearingValidationProcessor;

    @Mock
    JmsTemplate defaultJmsTemplate;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void test_Process () {

        HearingResulted hearingDetails = HearingResulted.builder()
                .jurisdictionType(JurisdictionType.CROWN)
                //.messageRetryCounter(10)
                .build();
        //when
        hearingResultedPublisher.publish(hearingDetails);

        //then
      //  verify(hearingResultedPublisher).publish(hearingDetails);



    }


}
