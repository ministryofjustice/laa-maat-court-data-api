package gov.uk.courtdata.reporder.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.reporder.service.RepOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@XRayEnabled
@Controller
@RequiredArgsConstructor
@Tag(name = "RepOrders", description = "Rest API for rep orders")
public class RepOrderGQLController {

    private final RepOrderService repOrderService;

    @QueryMapping
    public RepOrderEntity findByRepId(@Argument Integer repId) {
        return repOrderService.findByRepId(repId);
    }

}
