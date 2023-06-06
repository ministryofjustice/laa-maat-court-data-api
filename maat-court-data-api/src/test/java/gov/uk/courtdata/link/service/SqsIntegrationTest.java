package gov.uk.courtdata.link.service;

import gov.uk.courtdata.integration.MockServicesExcludingListenerConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

@SpringBootTest
@Import(MockServicesExcludingListenerConfig.class)
@Testcontainers @DirtiesContext
@TestPropertySource(locations = {"classpath:application.yaml"})
public abstract class SqsIntegrationTest {

    private static DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack");

    @Container
    public static LocalStackContainer LOCALSTACK_CONTAINER = new LocalStackContainer(LOCALSTACK_IMAGE)
            .withServices(SQS);

    /**
     * Just configures Localstack's SQS server endpoint in the application
     */
    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("cloud.aws.sqs.endpoint",
                () -> LOCALSTACK_CONTAINER.getEndpointOverride(SQS).toString());
    }

}
