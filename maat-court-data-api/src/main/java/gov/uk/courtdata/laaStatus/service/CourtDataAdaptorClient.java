package gov.uk.courtdata.laaStatus.service;

import feign.Param;
import feign.RequestLine;
import gov.uk.courtdata.model.Token;
import gov.uk.courtdata.model.laastatus.LaaStatusUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;


public interface CourtDataAdaptorClient {

    @RequestLine("POST /oauth/token?grant_type=client_credentials&client_id={client_id}&client_secret={client_secret}")
    ResponseEntity<Token> getOAuthToken(@Param("client_id") final String clientId, @Param("client_secret") final String clientSecret);

    @RequestLine("POST /api/internal/v1/laa_references")
    ResponseEntity<Void> postLaaStatusUpdate(final LaaStatusUpdate laaStatusUpdate);

}
