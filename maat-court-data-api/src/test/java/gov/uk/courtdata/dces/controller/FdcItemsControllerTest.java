package gov.uk.courtdata.dces.controller;

import gov.uk.courtdata.dces.request.CreateFdcItemRequest;
import gov.uk.courtdata.dces.service.FdcContributionsService;
import gov.uk.courtdata.entity.FdcItemsEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FdcItemsController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureWireMock(port = 9987)
class FdcItemsControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement/fdc-items";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FdcContributionsService fdcContributionsService;


    @Test
    void givenValidRequest_whenCreateFdcItems_thenReturnCreatedItem() throws Exception {
        CreateFdcItemRequest request = new CreateFdcItemRequest();
        when(fdcContributionsService.createFdcItems(any(CreateFdcItemRequest.class))).thenReturn(FdcItemsEntity.builder().id(1).build());

        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void givenNullRequest_whenCreateFdcItems_thenBadRequestError() throws Exception {
        CreateFdcItemRequest request = null;

        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenValidFdcId_whenDeleteFdcItems_thenReturnDeletedCount() throws Exception {
        when(fdcContributionsService.deleteFdcItems( 1)).thenReturn(1L);

        mvc.perform(MockMvcRequestBuilders.delete(String.format("%s/fdc-id/%d", ENDPOINT_URL, 1))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
            .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isEqualTo("1"));
    }

    @Test
    void givenNullFdcId_whenDeleteFdcItems_thenNotFoundError() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(String.format("%s/fdc-id/", ENDPOINT_URL))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }
    @Test
    void givenNonExistentFdcId_whenDeleteFdcItems_thenInternalServerError() throws Exception {
        doThrow(new EmptyResultDataAccessException(1)).when(fdcContributionsService).deleteFdcItems(999);

        mvc.perform(MockMvcRequestBuilders.delete(String.format("%s/fdc-id/%d", ENDPOINT_URL, 999))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void givenDataAccessException_whenDeleteFdcItems_thenInternalServerError() throws Exception {
        doThrow(new DataAccessException("Database error") {}).when(fdcContributionsService).deleteFdcItems(1);

        mvc.perform(MockMvcRequestBuilders.delete(String.format("%s/fdc-id/%d", ENDPOINT_URL, 1))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError());
    }


}