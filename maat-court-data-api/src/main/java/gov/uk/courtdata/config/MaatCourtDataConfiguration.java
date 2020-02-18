package gov.uk.courtdata.config;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MaatCourtDataConfiguration {

    @Bean
    public Gson gson() {
        return new Gson();
    }


}
