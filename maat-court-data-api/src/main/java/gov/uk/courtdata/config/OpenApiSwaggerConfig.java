package gov.uk.courtdata.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiSwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(@Value("version") String version) {
        return new OpenAPI()
                .info(new Info().title("Crime Apps - MAAT Court Data API")
                        .version(version)
                        .description("These are the the API for the LAA Maat Court Data.   ")
                        .termsOfService("https://www.gov.uk/government/organisations/legal-aid-agency")
                        .contact(new Contact().email("crimeapps-laa@digital.justice.gov.uk").name("Crime Apps Team"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                );
    }
}
