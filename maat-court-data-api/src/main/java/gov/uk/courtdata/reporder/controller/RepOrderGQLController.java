package gov.uk.courtdata.reporder.controller;

import gov.uk.courtdata.dto.RepOrderDTO;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.reporder.gqlfilter.RepOrderFilter;
import gov.uk.courtdata.reporder.service.RepOrderService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "RepOrdersGQL", description = "Rest API for rep orders")
public class RepOrderGQLController implements GraphQLQueryResolver {

    private final RepOrderService repOrderService;

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @QueryMapping
    public RepOrderDTO findByRepId(@Argument Integer repId) {
        LoggingData.MAAT_ID.putInMDC(repId);
        return repOrderService.find(repId, false);
    }


    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @QueryMapping
    public RepOrderDTO findByRepOrderFilter(@Argument("filter") RepOrderFilter filter) {
        LoggingData.MAAT_ID.putInMDC(filter.getId());
        RepOrderDTO repOrderDTO = null;
        if (filter.getId() > 0) {
            repOrderDTO = repOrderService.find(filter.getId(), Boolean.valueOf(filter.getSentenceOrderDate()));
        }
        return repOrderDTO;
    }

}
