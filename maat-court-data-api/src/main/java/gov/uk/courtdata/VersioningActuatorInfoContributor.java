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

  private final VersionMetadata versionMetaData;

  @Override
  public void contribute(Info.Builder infoBuilder) {
    Map<String, String> additionalVersionMetaData = new HashMap<>();

    String semanticVersion = versionMetaData.getSemanticVersion();
    additionalVersionMetaData.put("semanticVersion", semanticVersion);

    Map<String, String> gitVersionProperties = Arrays.stream(GitProperty.values())
        .collect(Collectors.toMap(GitProperty::getGitPropertyKey,
            versionMetaData::getGitPropertyValue));

    additionalVersionMetaData.putAll(gitVersionProperties);

    infoBuilder.withDetail("versionMetaData", additionalVersionMetaData);
  }
}
