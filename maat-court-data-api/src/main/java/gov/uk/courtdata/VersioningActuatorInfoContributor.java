package gov.uk.courtdata;

import gov.uk.courtdata.util.GitProperty;
import gov.uk.courtdata.util.VersionMetadata;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class VersioningActuatorInfoContributor implements InfoContributor {

  private static final String SEMANTIC_VERSION = "semanticVersion";
  private static final String VERSION_META_DATA = "versionMetaData";
  
  private final VersionMetadata versionMetadata;

  @Override
  public void contribute(Info.Builder infoBuilder) {
    Map<String, String> additionalVersionMetaData = new HashMap<>();

    String semanticVersion = versionMetadata.getSemanticVersion();
    additionalVersionMetaData.put(SEMANTIC_VERSION, semanticVersion);

    Map<String, String> gitVersionProperties = Arrays.stream(GitProperty.values())
        .collect(Collectors.toMap(GitProperty::getGitPropertyKey,
            versionMetadata::getGitPropertyValue));

    additionalVersionMetaData.putAll(gitVersionProperties);
    infoBuilder.withDetail(VERSION_META_DATA, additionalVersionMetaData);
  }
}
