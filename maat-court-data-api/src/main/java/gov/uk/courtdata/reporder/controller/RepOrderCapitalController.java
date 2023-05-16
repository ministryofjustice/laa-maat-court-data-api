package gov.uk.courtdata.reporder.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.common.dto.ErrorDTO;
import gov.uk.courtdata.reporder.service.RepOrderCapitalService;
import gov.uk.courtdata.validator.MaatIdValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("${api-endpoints.assessments-domain}/rep-orders/capital")
@Slf4j
@RequiredArgsConstructor
@XRayEnabled
@Tag(name = "RepOrders", description = "Rest API for RepOrder Capital")
public class RepOrderCapitalController {

    private final RepOrderCapitalService service;

    private final MaatIdValidator maatIdValidator;

    @RequestMapping(value = "/reporder/{repId}",
            method = RequestMethod.HEAD,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(description = "Retrieve a rep order capital record")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @ApiResponse(responseCode = "400",
            description = "Bad Request.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    @ApiResponse(responseCode = "500",
            description = "Server Error.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    public ResponseEntity<HttpHeaders> getCapitalAssetCount(@PathVariable int repId) {
        log.info("Rep Order Capital Asset Count Request Received");
        maatIdValidator.validate(repId);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentLength(service.getCapitalAssetCount(repId));
        return ResponseEntity.ok().headers(responseHeaders).build();
    }
}
