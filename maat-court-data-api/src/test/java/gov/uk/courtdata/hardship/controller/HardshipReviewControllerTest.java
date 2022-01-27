package gov.uk.courtdata.hardship.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.hardship.service.HardshipReviewService;
import gov.uk.courtdata.validator.HardshipReviewIdValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(HardshipReviewController.class)
public class HardshipReviewControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private HardshipReviewService hardshipReviewService;

    @MockBean
    private HardshipReviewIdValidator hardshipReviewIdValidator;

    private final Integer MOCK_HARDSHIP_ID = 1000;

    private final String endpointUrl = "/hardship";

    @Test
    public void givenCorrectParameters_whenGetHardshipIsInvoked_thenHardshipIsReturned() throws Exception {

        HardshipReviewDTO hardshipReviewDTO = TestModelDataBuilder.getHardshipReviewDTOWithDetailsAndProgress();

        when(hardshipReviewService.find(MOCK_HARDSHIP_ID)).thenReturn(hardshipReviewDTO);

        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + "/" + MOCK_HARDSHIP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(String.valueOf(MOCK_HARDSHIP_ID)))
                .andExpect(jsonPath("$.reviewDetails[0].hardshipReviewId").value(String.valueOf(MOCK_HARDSHIP_ID)))
                .andExpect(jsonPath("$.reviewProgresses[0].hardshipReviewId").value(String.valueOf(MOCK_HARDSHIP_ID)));
    }

    @Test
    public void givenIncorrectParameters_whenGetHardshipIsInvoked_thenErrorIsThrown() throws Exception {
        when(hardshipReviewIdValidator.validate(any(Integer.class))).thenThrow(new ValidationException());
        when(hardshipReviewService.find(MOCK_HARDSHIP_ID)).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.post(endpointUrl + "/" + MOCK_HARDSHIP_ID))
                .andExpect(status().is4xxClientError());
    }
}
