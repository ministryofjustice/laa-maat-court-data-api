package gov.uk.courtdata.hearing.processor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HearingWQProcessorTest {

    @InjectMocks
    HearingWQProcessor hearingWQProcessor;




    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);



    }


    @Test
    public void process() {
    }
}
