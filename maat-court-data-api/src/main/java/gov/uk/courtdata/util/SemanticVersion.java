package gov.uk.courtdata.util;

import graphql.com.google.common.collect.ImmutableSortedSet;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

@Slf4j
class SemanticVersion {

  private static final String SEMVER_VERSION_FILE_NAME = "build/semver/version.txt";

  private final Set<String> uniqueSemanticVersions;

  SemanticVersion() {
    this(SEMVER_VERSION_FILE_NAME);
  }

  SemanticVersion(String pathToSemverVersionFile) {
    Set<String> loadedSemanticVersions = null;
    File file = Paths.get(pathToSemverVersionFile).toFile();

    try {
      List<String> semanticVersions = FileUtils.readLines(file, Charset.defaultCharset());
      loadedSemanticVersions = ImmutableSortedSet.copyOf(semanticVersions);
    } catch (IOException e) {
      log.info("Unable to read the Semantic Version file [{}] due to: {}", pathToSemverVersionFile,
          e.getMessage(), e);
    }

    if (Objects.isNull(loadedSemanticVersions)) {
      uniqueSemanticVersions = Collections.emptySet();
    } else {
      uniqueSemanticVersions = loadedSemanticVersions;
    }
  }

  public String asText() {
    return String.join("|", uniqueSemanticVersions);
  }
}
