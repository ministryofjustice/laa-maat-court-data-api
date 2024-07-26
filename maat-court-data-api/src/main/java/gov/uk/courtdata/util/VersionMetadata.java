package gov.uk.courtdata.util;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VersionMetadata {

  private final SemanticVersion semanticVersion;
  private final GitProperties gitProperties;

  @NotNull
  public String getSemanticVersion() {
    return semanticVersion.asText();
  }

  @NotNull
  public String getGitPropertyValue(GitProperty gitProperty) {
    return gitProperties.getValueOf(gitProperty);
  }
}
