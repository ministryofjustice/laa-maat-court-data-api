package gov.uk.courtdata.address.service;

import gov.uk.courtdata.address.entity.Address;
import gov.uk.courtdata.address.repository.AddressRepository;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AddressService.class)
class AddressServiceTest {

    private static final int ID = 1;
    @MockBean
    private AddressRepository addressRepository;
    private AddressService addressService;

    @BeforeEach
    void setUp() {
        addressService = new AddressService(addressRepository);
    }

    @Test
    void givenAValidInput_whenFindIsInvoked_thenShouldReturnAddressDTO() {
        when(addressRepository.findById(anyInt())).thenReturn(Optional.of(Address.builder().id(ID).build()));
        addressService.find(ID);
        verify(addressRepository, atLeastOnce()).findById(ID);
    }

    @Test
    void givenAddressNotFound_whenFindIsInvoked_thenExceptionIsRaised() {
        assertThatThrownBy(() -> addressService.find(ID)).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Address not found for id ");
    }

    @Test
    void givenAValidInput_whenUpdateIsInvoked_thenUpdateIsSuccess() {
        when(addressRepository.findById(anyInt())).thenReturn(Optional.of(Address.builder().id(ID).build()));
        HashMap<String, Object> inputMap = new HashMap<>();
        inputMap.put("city", "test");
        addressService.update(ID, inputMap);
        verify(addressRepository, atLeastOnce()).findById(any());
        verify(addressRepository, atLeastOnce()).save(any());
    }


    @Test
    void givenAValidInput_whenCreateIsInvoked_thenCreateIsSuccess() {
        addressService.create(Address.builder().id(ID).build());
        verify(addressRepository, atLeastOnce()).saveAndFlush(any());
    }

    @Test
    void givenAValidInput_whenDeleteIsInvoked_thenDeleteIsSuccess() {
        addressService.delete(ID);
        verify(addressRepository, atLeastOnce()).deleteById(any());
    }

}
