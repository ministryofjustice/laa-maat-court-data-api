package gov.uk.courtdata.address.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.address.service.AddressService;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.constants.ErrorCodes;
import gov.uk.courtdata.address.entity.Address;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressController.class)
@AutoConfigureMockMvc(addFilters = false)
class AddressControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/address";
    private static final int ID = 1;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AddressService addressService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenCorrectId_whenGetAddressIsInvoked_thenResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + ID))
                .andExpect(status().isOk());
        verify(addressService).find(ID);
    }

    @Test
    void givenInternalServerError_whenGetAddressIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        when(addressService.find(ID))
                .thenThrow(EmptyResultDataAccessException.class);
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + ID)
                        .content(objectMapper.writeValueAsString(Address.builder().id(ID).build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCodes.DB_ERROR));
    }


    @Test
    void givenValidRequest_whenUpdateAddressIsInvoked_thenUpdateIsSuccess() throws Exception {
        mvc.perform(MockMvcRequestBuilders.patch(ENDPOINT_URL + "/" + ID)
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getAddress(ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenAEmptyContent_whenUpdateAddressIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.patch(ENDPOINT_URL + "/" + ID)
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenInValidRequest_whenUpdateAddressIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        doThrow(new RequestedObjectNotFoundException("Address not found")).when(addressService).update(any(), any());
        mvc.perform(MockMvcRequestBuilders.patch(ENDPOINT_URL + "/" + ID)
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getAddress(ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void givenInternalServerError_whenUpdateAddressIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        doThrow(EmptyResultDataAccessException.class).when(addressService).update(any(), any());
        mvc.perform(MockMvcRequestBuilders.patch(ENDPOINT_URL + "/" + ID)
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getAddress(ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCodes.DB_ERROR));
    }

    @Test
    void givenCorrectId_whenDeleteAddressIsInvoked_thenResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(ENDPOINT_URL + "/" + ID))
                .andExpect(status().isOk());
        verify(addressService).delete(ID);
    }

    @Test
    void givenInternalServerError_whenDeleteAddressIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        doThrow(EmptyResultDataAccessException.class).when(addressService).delete(anyInt());
        mvc.perform(MockMvcRequestBuilders.delete(ENDPOINT_URL + "/" + ID)
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getAddress(ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCodes.DB_ERROR));
    }

    @Test
    void givenValidRequest_whenCreateAddressIsInvoked_thenUpdateIsSuccess() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getAddress(ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenAEmptyContent_whenCreateAddressIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenInValidRequest_whenCreateAddressIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        doThrow(new RequestedObjectNotFoundException("Address not found")).when(addressService).create(any());
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getAddress(ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void givenInternalServerError_whenCreateAddressIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        doThrow(EmptyResultDataAccessException.class).when(addressService).create(any());
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getAddress(ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCodes.DB_ERROR));
    }

}
