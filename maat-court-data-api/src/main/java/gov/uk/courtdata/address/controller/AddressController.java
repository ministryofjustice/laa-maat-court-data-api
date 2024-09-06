package gov.uk.courtdata.address.controller;

import gov.uk.courtdata.address.entity.Address;
import gov.uk.courtdata.address.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Address", description = "Rest API for addresses")
@RequestMapping("${api-endpoints.address-domain}")
public class AddressController {

    private final AddressService addressService;


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve an Address record")
    @StandardApiResponseCodes
    public ResponseEntity<Address> getAddress(@PathVariable int id) {
        log.info("Get Address Request Received");
        return ResponseEntity.ok(addressService.find(id));
    }

    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update an Address record")
    @StandardApiResponseCodes
    public ResponseEntity<Void> updateAddress(@PathVariable int id, @RequestBody Map<String, Object> updatedFields) {
        log.info("Update Address Request Received");
        addressService.update(id, updatedFields);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Delete an Address record")
    @StandardApiResponseCodes
    public ResponseEntity<Void> deleteAddress(@PathVariable int id) {
        log.info("Delete Address Request Received");
        addressService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create an Address record")
    @StandardApiResponseCodes
    public ResponseEntity<Address> createAddress(@RequestBody @Valid Address address) {
        log.info("Create Address Request Received");
        return ResponseEntity.ok(addressService.create(address));
    }

}
