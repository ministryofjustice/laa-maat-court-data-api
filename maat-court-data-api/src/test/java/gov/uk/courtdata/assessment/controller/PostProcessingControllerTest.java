package gov.uk.courtdata.assessment.controller;

import gov.uk.courtdata.assessment.service.PostProcessingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PostProcessingController.class)
public class PostProcessingControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PostProcessingService postProcessingService;

    private static final String MOCK_REP_ID = "1234";

    @Value("${api-endpoints.assessments-domain}")
    private String assessmentsDomain;

    private final String ENDPOINT_URL = assessmentsDomain + "post-processing/";

    @Test
    public void givenValidRepId_whenDoPostProcessingIsInvoked_thenPostProcessingIsPerformed() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + MOCK_REP_ID))
                .andExpect(status().isOk());
        verify(postProcessingService).execute(anyInt());
    }

    @Test
    public void givenInvalidRepId_whenDoPostProcessingIsInvoked_thenErrorIsThrown() throws Exception {

    }
}
