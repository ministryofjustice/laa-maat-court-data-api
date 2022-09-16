package gov.uk.courtdata.repOrder.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import gov.uk.courtdata.repOrder.service.RepOrderService;
import gov.uk.courtdata.repOrder.validator.UpdateAppDateCompletedValidator;
import gov.uk.courtdata.repository.RepOrderRepository;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@XRayEnabled
@RestController
@RequiredArgsConstructor
@Tag(name = "RepOrders", description = "Rest API for rep orders")
@RequestMapping("${api-endpoints.assessments-domain}/rep-orders")
public class RepOrderController {

    private final RepOrderService repOrderService;
    private final UpdateAppDateCompletedValidator updateAppDateCompletedValidator;
    private final RepOrderRepository repOrderRepository;

    @Value("classpath:schema.graphql")
    private Resource schemaResource;

    private GraphQL graphQL;

    @PostConstruct
    public void loadSchema() {
        try {
            InputStream schemaFile = schemaResource.getInputStream();
            TypeDefinitionRegistry registry = new SchemaParser().parse(schemaFile);
            RuntimeWiring wiring = buildWiring();
            GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(registry, wiring);
            graphQL = GraphQL.newGraphQL(schema).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private RuntimeWiring buildWiring() {
        DataFetcher<List<RepOrderEntity>> f1 = d -> {
            return (List<RepOrderEntity>) repOrderRepository.findAll();
        };

        DataFetcher<RepOrderEntity> f2 = d -> {
            System.out.println("REP_ID ******************* : " + d.getArgument("repId"));
            return (RepOrderEntity) repOrderRepository.findById(d.getArgument("repId")).get();
        };

        return RuntimeWiring.newRuntimeWiring().type("Query",
                typeWriting -> typeWriting.dataFetcher("findAll", f1).
                        dataFetcher("findById", f2)).build();
    }

    @PostMapping("/getAll")
    public ResponseEntity<Object> getAll(@RequestBody String query) {
        ExecutionResult r = graphQL.execute(query);
        return new ResponseEntity<Object>(r, HttpStatus.OK);
    }

    @PostMapping("/getRepOrderById")
    @Transactional
    public ResponseEntity<Object> getRepOrderById(@RequestBody String query) {
        System.out.println("GRAPHQL query : " + query);
        ExecutionResult r = graphQL.execute(query);
        return new ResponseEntity<Object>(r, HttpStatus.OK);
    }

    @GetMapping(
            value = "/{repId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(description = "Retrieve a rep order record")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @ApiResponse(responseCode = "400",
            description = "Bad Request.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    @ApiResponse(responseCode = "500",
            description = "Server Error.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    public ResponseEntity<Object> getRepOrder(@PathVariable int repId) {
        log.info("Get Rep Order Request Received");
        return ResponseEntity.ok(repOrderService.find(repId));
    }


    @PostMapping(value = "/update-date-completed",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(description = "Update application date completed")
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
    public ResponseEntity<Object> updateApplicationDateCompleted(
            @Parameter(description = "Update app date",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UpdateAppDateCompleted.class)
                    )
            ) @RequestBody UpdateAppDateCompleted updateAppDateCompleted) {

        log.debug("Assessments Request Received for repId : {}", updateAppDateCompleted.getRepId());
        updateAppDateCompletedValidator.validate(updateAppDateCompleted);
        repOrderService.updateAppDateCompleted(updateAppDateCompleted);
        return ResponseEntity.ok().build();
    }
}
