package gov.uk.courtdata.courtdataadapter.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class CourtDataAdapterClientConfig {
    @Value("${cda.laastatus.url}")
    private String laaStatusUrl;
    @Value("${cda.hearing.url}")
    private String hearingUrl;
}
