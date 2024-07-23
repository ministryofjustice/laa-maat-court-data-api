package gov.uk.courtdata;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SemanticVersioningActuatorInfoContributor implements InfoContributor {

  public static final String SEMVER_VERSION_TXT = "build/semver/version.txt";

  public static void main(String[] args) {
    SemanticVersioningActuatorInfoContributor semanticVersioningActuatorInfoContributor = new SemanticVersioningActuatorInfoContributor();
    @NotNull List<String> string = semanticVersioningActuatorInfoContributor.getSemanticVersions();
    System.out.println("Found: " + string);
  }

  @Override
  public void contribute(Info.Builder builder) {
    @NotNull List<String> semanticVersions = getSemanticVersions();

    Map<String, Object> customInfo = new HashMap<>();
    for (int i = 0; i < semanticVersions.size(); i++) {
      customInfo.put("line" + i + 1, semanticVersions);
    }

    builder.withDetail("semanticVersion", customInfo);
  }

  public @NotNull List<String> getSemanticVersions() {
    File file = Paths.get(SEMVER_VERSION_TXT).toFile();

    if (file.exists() && file.canRead()) {
      try {
        return FileUtils.readLines(file, Charset.defaultCharset());
      } catch (IOException e) {
        log.info("Unable to read the Semantioc Version file: {}",
            SEMVER_VERSION_TXT,
            e);
      }
    }

    return Collections.emptyList();
  }
}
