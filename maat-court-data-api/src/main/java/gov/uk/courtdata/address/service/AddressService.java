package gov.uk.courtdata.address.service;

import gov.uk.courtdata.address.repository.AddressRepository;
import gov.uk.courtdata.address.entity.Address;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.helper.ReflectionHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public Address find(Integer id) {
        log.info("AddressService::find - Start");
        return addressRepository.findById(id)
                .orElseThrow(() -> new RequestedObjectNotFoundException(String.format("Address not found for id %d", id)));
    }

    public void update(Integer id, Map<String, Object> address) {
        log.info("AddressService::update - Start");
        Address currentAddress = addressRepository.findById(id)
                .orElseThrow(() -> new RequestedObjectNotFoundException(String.format("Address not found for id %d", id)));

        ReflectionHelper.updateEntityFromMap(currentAddress, address);
        addressRepository.save(currentAddress);
    }

    public void delete(Integer id) {
        log.info("AddressService::delete - Start");
        addressRepository.deleteById(id);
    }

    public Address create(Address address) {
        log.info("AddressService::create - Start");
        return addressRepository.saveAndFlush(address);
    }

}
