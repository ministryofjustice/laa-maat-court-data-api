package gov.uk.courtdata.util;

import gov.uk.courtdata.enums.LoggingData;
import io.sentry.EventProcessor;
import io.sentry.Hint;
import io.sentry.SentryEvent;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SentryAlertEnricher implements EventProcessor {

  private final VersionMetadata versionMetadata;

  @Override
  public SentryEvent process(@NotNull SentryEvent sentryEvent, @NotNull Hint hint) {

    sentryEvent.setRelease(versionMetadata.getSemanticVersion());
    sentryEvent.setTransaction(LoggingData.LAA_TRANSACTION_ID.getValueFromMDC());

    addMdcContextTo(sentryEvent);
    addGitMetaDataTo(sentryEvent);

    return sentryEvent;
  }

  private void addGitMetaDataTo(@NotNull SentryEvent sentryEvent) {
    Arrays.stream(GitProperty.values()).forEach(gitProperty -> {
      String tagName = gitProperty.getGitPropertyKey();
      String tagValue = versionMetadata.getGitPropertyValue(gitProperty);

      sentryEvent.setTag(tagName, tagValue);
    });
  }

  private void addMdcContextTo(@NotNull SentryEvent sentryEvent) {
    Arrays.stream(LoggingData.values()).forEach(loggingData -> {
      String tagName = loggingData.getKey();
      String tagValue = loggingData.getValueFromMDC();

      sentryEvent.setTag(tagName, tagValue);
    });
  }
}
