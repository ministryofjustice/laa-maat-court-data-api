package gov.uk.courtdata.util;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VersionMetadata {

  private final SemanticVersion semanticVersion;
  private final GitProperties gitProperties;

  public VersionMetadata() {
    semanticVersion = new SemanticVersion();
    gitProperties = new GitProperties();
  }

  public @NotNull String getSemanticVersion() {
    return semanticVersion.asText();
  }

  public @NotNull String getGitPropertyValue(GitProperty gitProperty) {
    return gitProperties.getValueOf(gitProperty);
  }
}
