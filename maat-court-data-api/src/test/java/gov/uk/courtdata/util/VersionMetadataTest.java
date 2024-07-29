package gov.uk.courtdata.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VersionMetadataTest {

  @Mock
  private SemanticVersion mockSemanticVersion;

  @Mock
  private GitPropertiesFile mockGitPropertiesFile;

  private VersionMetadata versionMetadata;

  @BeforeEach
  void setUp() {
    versionMetadata = new VersionMetadata(mockSemanticVersion, mockGitPropertiesFile);
  }

  @Test
  void getSemanticVersion_shouldReturnSemanticVersion() {
    final String semanticVersion = "1.0.0";
    when(mockSemanticVersion.asText())
        .thenReturn(semanticVersion);

    String actualSemanticVersion = versionMetadata.getSemanticVersion();

    assertEquals(semanticVersion, actualSemanticVersion);
  }

  @Test
  void getGitPropertyValue_shouldReturnGitPropertyValue() {
    final String gitCommitIdValue = "4ddf8ddc55a6348fefea65554bf8316dd4ffdb20";
    when(mockGitPropertiesFile.getValueOf(GitProperty.GIT_COMMIT_ID))
        .thenReturn(gitCommitIdValue);

    String actualGitPropertyValue = versionMetadata.getGitPropertyValue(GitProperty.GIT_COMMIT_ID);

    assertEquals(gitCommitIdValue, actualGitPropertyValue);
  }
}
