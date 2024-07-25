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

  private static final String GIT_COMMIT_ID = "gitCommitId";

  private final VersionMetaData versionMetaData;

  @Override
  public SentryEvent process(@NotNull SentryEvent sentryEvent, @NotNull Hint hint) {

    sentryEvent.setRelease(versionMetaData.getSemanticVersion());
    sentryEvent.setTag(GIT_COMMIT_ID, versionMetaData.getGitPropertyValue(GitProperty.GIT_COMMIT_ID));
    sentryEvent.setTransaction(LoggingData.LAA_TRANSACTION_ID.getValueFromMDC());

    addMdcContextTo(sentryEvent);

    return sentryEvent;
  }

  private void addMdcContextTo(@NotNull SentryEvent sentryEvent) {
    Arrays.stream(LoggingData.values()).forEach(loggingData -> {
      String tagName = loggingData.getKey();
      String tagValue = loggingData.getValueFromMDC();
      sentryEvent.setTag(tagName, tagValue);
    });
  }
}
