package gov.uk.courtdata;

import gov.uk.courtdata.util.VersionMetaData;
import gov.uk.courtdata.util.VersionMetaData.GitProperty;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class VersioningActuatorInfoContributor implements InfoContributor {

  private final VersionMetaData versionMetaData;

  @Override
  public void contribute(Info.Builder builder) {
    @NotNull List<String> semanticVersions = versionMetaData.getSemanticVersions();
    System.out.println("Found: " + semanticVersions);

    Map<String, Object> customInfo = new HashMap<>();
    for (int i = 0; i < semanticVersions.size(); i++) {
      customInfo.put("line" + i + 1, semanticVersions);
    }

    Arrays.stream(GitProperty.values()).forEach(new Consumer<GitProperty>() {
      @Override
      public void accept(GitProperty gitProperty) {
        String key = gitProperty.getGitPropertyKey();
        String value = versionMetaData.getGitPropertyValue(gitProperty);
        customInfo.put(key, value);
      }
    });

    builder.withDetail("semanticVersion", customInfo);
  }


}
