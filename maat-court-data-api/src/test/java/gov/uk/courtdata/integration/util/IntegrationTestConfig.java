package gov.uk.courtdata.integration.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

@Configuration
public class IntegrationTestConfig {

    @Autowired
    private IntegrationTestPropertySource integrationTestPropertySource;

    @Autowired
    public void configureEnvironment(ConfigurableEnvironment environment) {
        environment.getPropertySources().addFirst(integrationTestPropertySource);
    }
}
