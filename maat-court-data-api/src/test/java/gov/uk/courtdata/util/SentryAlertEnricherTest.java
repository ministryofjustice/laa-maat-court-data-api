package gov.uk.courtdata.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.enums.LoggingData;
import io.sentry.Hint;
import io.sentry.SentryEvent;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

@ExtendWith(MockitoExtension.class)
class SentryAlertEnricherTest {

    @Mock
    private VersionMetadata mockVersionMetadata;

    private SentryAlertEnricher sentryAlertEnricher;

    @BeforeEach
    void setUp() {
        MDC.clear();
        sentryAlertEnricher = new SentryAlertEnricher(mockVersionMetadata);
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void testProcessShouldPopulateSentryEvent() {
        final String semanticVersion = "1.0.0";
        final String testGitPropertyValue = "testGitPropertyValue";
        final String testLoggingValue = "testLoggingValue";

        when(mockVersionMetadata.getSemanticVersion()).thenReturn(semanticVersion);
        when(mockVersionMetadata.getGitPropertyValue(any())).thenReturn(testGitPropertyValue);
        for (LoggingData loggingData : LoggingData.values()) {
            MDC.put(loggingData.getKey(), testLoggingValue);
        }

        SentryEvent actualSentryEvent = sentryAlertEnricher.process(new SentryEvent(), new Hint());

        assertThat(actualSentryEvent.getRelease()).isEqualTo(semanticVersion);
        assertThat(actualSentryEvent.getTransaction()).isEqualTo(testLoggingValue);

        for (GitProperty gitProperty : GitProperty.values()) {
            assertThat(actualSentryEvent.getTag(gitProperty.getGitPropertyKey()))
                    .isEqualTo(testGitPropertyValue);
        }

        for (LoggingData loggingData : LoggingData.values()) {
            assertThat(actualSentryEvent.getTag(loggingData.getKey())).isEqualTo(testLoggingValue);
        }
    }
}
