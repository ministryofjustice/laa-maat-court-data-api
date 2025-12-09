package gov.uk.courtdata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.util.GitProperty;
import gov.uk.courtdata.util.VersionMetadata;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.info.Info;

@ExtendWith(MockitoExtension.class)
class VersioningActuatorInfoContributorTest {

  @Mock
  private VersionMetadata mockVersionMetadata;

  private VersioningActuatorInfoContributor infoContributor;

  @BeforeEach
  void setUp() {
    infoContributor = new VersioningActuatorInfoContributor(mockVersionMetadata);
  }

  @Test
  void contribute_shouldAddVersionMetaDataToInfoBuilder() {
    final String semanticVersion = "1.0.0";
    final String gitCommitId = "4ddf8ddc55a6348fefea65554bf8316dd4ffdb20";

    when(mockVersionMetadata.getSemanticVersion())
        .thenReturn(semanticVersion);

    when(mockVersionMetadata.getGitPropertyValue(any(GitProperty.class)))
        .thenAnswer(invocation -> {
          GitProperty gitProperty = invocation.getArgument(0);
          if (GitProperty.GIT_COMMIT_ID.equals(gitProperty)) {
            return gitCommitId;
          }
          return StringUtils.EMPTY;
        });

    Info.Builder infoBuilder = new Info.Builder();

    infoContributor.contribute(infoBuilder);

    Info info = infoBuilder.build();
    Map<String, Object> infoDetails = info.getDetails();

    Object rawVersionMetaData = infoDetails.get("versionMetaData");
    assertThat(rawVersionMetaData).isInstanceOf(Map.class);

    Map<String, String> versionMetaData = (Map<String, String>) rawVersionMetaData;

    assertThat(versionMetaData)
            .as("metadata should contain expected version fields")
            .containsKeys("semanticVersion", "git.commit.id")
            .containsEntry("semanticVersion", semanticVersion)
            .containsEntry("git.commit.id", gitCommitId);
  }
}