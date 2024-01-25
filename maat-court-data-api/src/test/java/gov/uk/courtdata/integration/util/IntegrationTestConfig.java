package gov.uk.courtdata.integration.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

@Configuration
public class IntegrationTestConfig {

    @Autowired
    public void configureEnvironment(Environment env) {
        if (env instanceof ConfigurableEnvironment) {
            ((ConfigurableEnvironment) env).getPropertySources().addFirst(new OAuth2TokenUriPropertySource());
        }
    }
}
