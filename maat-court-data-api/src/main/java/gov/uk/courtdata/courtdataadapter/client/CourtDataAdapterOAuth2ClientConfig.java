package gov.uk.courtdata.courtdataadapter.client;

import gov.uk.courtdata.exception.ApiClientException;
import gov.uk.courtdata.exception.RetryableWebClientResponseException;
import io.netty.handler.timeout.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import org.springframework.web.client.HttpServerErrorException;

import reactor.util.retry.Retry;
import java.time.Duration;

/**
 * <code>CourtDataAdapterOAuth2ClientConfig</code>
 */
@Configuration
@Slf4j
public class CourtDataAdapterOAuth2ClientConfig {

    private static final String REGISTERED_ID = "cda";
    @Value("${cda.retry-config.max-retries}") private Integer maxRetries;
    @Value("${cda.retry-config.min-back-off-period}") private Integer minBackoffPeriod;
    @Value("${cda.retry-config.jitter-value}") private Double jitter;

    /**
     * @param tokenUri
     * @param clientId
     * @param clientSecret
     * @return
     */
    @Bean
    ClientRegistrationRepository getRegistration(
            @Value("${spring.security.oauth2.client.provider.cda.token-uri}") String tokenUri,
            @Value("${spring.security.oauth2.client.registration.cda.client-id}") String clientId,
            @Value("${spring.security.oauth2.client.registration.cda.client-secret}") String clientSecret
    ) {
        ClientRegistration registration = ClientRegistration
                .withRegistrationId(REGISTERED_ID)
                .tokenUri(tokenUri)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .build();
        return new InMemoryClientRegistrationRepository(registration);
    }


    /**
     * @param clientRegistrationRepository
     * @return
     */
    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository) {

        // grant_type = client_credentials flow.
        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        // Machine to machine service.
        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository));
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }


    /**
     * @param authorizedClientManager
     * @return
     */
    @Bean(name = "cdaOAuth2WebClient")
    public WebClient webClient(@Value("${cda.url}") String baseUrl, OAuth2AuthorizedClientManager authorizedClientManager) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth2Client.setDefaultClientRegistrationId(REGISTERED_ID);
        return WebClient.builder()
                .baseUrl(baseUrl)
                .filter(loggingRequest())
                .filter(loggingResponse())
                .filter(oauth2Client)
                .filter(retryFilter(maxRetries, minBackoffPeriod, jitter))
                .filter(errorResponse())
                .build();
    }

    /**
     *
     * @return
     */
    private ExchangeFilterFunction loggingRequest() {
        return (clientRequest, next) -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return next.exchange(clientRequest);
        };
    }

    /**
     *
     * @return
     */
    private ExchangeFilterFunction loggingResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Response status: {}",clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }

    public static ExchangeFilterFunction errorResponse() {
        return ExchangeFilterFunctions.statusError(
                HttpStatus::isError, r -> {
                    String errorMessage =
                            String.format("Received error %s due to %s", r.statusCode().value(), r.statusCode().getReasonPhrase());
                    if (r.statusCode().is5xxServerError()) {
                        return new HttpServerErrorException(
                                r.statusCode(),
                                errorMessage
                        );
                    }
                    return new ApiClientException(errorMessage);
                });
    }

    /**
     * Retry exchange filter function.
     * Retries error responses on <code>RetryableWebClientResponseException</code> and request timeouts.
     * Progress is logged to the console after each retry and after all retries are exhausted at INFO level.
     * The number of retries, jitter and back of period are provided via fields.
     *
     * This method was copied from the following class in the laa-crime-commons library. This class was not
     * implemented as the laa-crime-commons library has prerequisites of JDK 17 and Spring Boot 3 that
     * are not implemented yet in this project.
     * https://github.com/ministryofjustice/laa-crime-commons/blob/5f0b82a23ef5a10d3b203acfde7ed92d0ce08895/crime-commons-spring-boot-starter-rest-client/src/main/java/uk/gov/justice/laa/crime/commons/filters/WebClientFilters.java
     *
     * @return the exchange filter function
     */
    public static ExchangeFilterFunction retryFilter(Integer maxRetries, Integer minBackoffPeriod, Double jitter) {
        return (request, next) ->
                next.exchange(request)
                        .retryWhen(
                                Retry.backoff(
                                                maxRetries,
                                                Duration.ofSeconds(
                                                        minBackoffPeriod
                                                )
                                        )
                                        .jitter(jitter)
                                        .filter(
                                                throwable ->
                                                        throwable instanceof RetryableWebClientResponseException ||
                                                                (throwable instanceof WebClientRequestException
                                                                        && throwable.getCause() instanceof TimeoutException) ||
                                                                throwable instanceof HttpServerErrorException
                                        ).onRetryExhaustedThrow(
                                                (retryBackoffSpec, retrySignal) ->
                                                        new ApiClientException(
                                                                String.format(
                                                                        "Call to service failed. Retries exhausted: %d/%d.",
                                                                        retrySignal.totalRetries(), maxRetries
                                                                ), retrySignal.failure()
                                                        )
                                        ).doAfterRetry(
                                                retrySignal -> {
                                                    if (retrySignal.totalRetries() > 0) {
                                                        log.warn(
                                                                String.format("Call to service failed, retrying: %d/%d",
                                                                        retrySignal.totalRetries(), maxRetries
                                                                )
                                                        );
                                                    }
                                                }
                                        )
                        );
    }

}
