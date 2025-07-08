package gov.uk.courtdata.billing.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.uk.courtdata.billing.service.MaatReferenceService;
import gov.uk.courtdata.exception.RecordsAlreadyExistException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(MaatReferenceExtractionController.class)
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class MaatReferenceExtractionControllerTest {
    private static final String ENDPOINT_URL = "/api/internal/v1/billing/maat-references";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private MaatReferenceService maatReferenceService;

    @Test
    void givenNoInput_whenPopulateMaatReferencesToExtract_thenSuccessResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL))
            .andExpect(status().isOk());
        verify(maatReferenceService).populateTable();
    }
    
    @Test
    void givenRecordsAlreadyExist_whenPopulateMaatReferencesToExtract_thenReturnError() throws Exception {
        when(maatReferenceService.populateTable()).thenThrow(RecordsAlreadyExistException.class);

        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL))
            .andExpect(status().isInternalServerError());
        verify(maatReferenceService).populateTable();
    }

    @Test
    void givenNoInput_whenDeleteMaatReferences_thenSuccessResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(ENDPOINT_URL))
            .andExpect(status().isOk());
        verify(maatReferenceService).deleteMaatReferences();
    }
}