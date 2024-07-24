package gov.uk.courtdata.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VersionMetaData {

  private static final String SEMVER_VERSION_FILE_NAME = "build/semver/version.txt";
  private static final String GIT_PROPERTIES_FILE_NAME = "git.properties";

  private final Properties gitProperties;

  public VersionMetaData() {
    ClassPathResource resource = new ClassPathResource(GIT_PROPERTIES_FILE_NAME);
    Properties loadedGitProperties = null;
    try {
      loadedGitProperties = PropertiesLoaderUtils.loadProperties(resource);
    } catch (IOException e) {
      log.info("Unable to load git properties file [{}] due to: {}", GIT_PROPERTIES_FILE_NAME,
          e.getMessage(),
          e);
    }
    if (Objects.isNull(loadedGitProperties)) {
      gitProperties = new Properties();
    } else {
      gitProperties = loadedGitProperties;
    }
  }

  public @NotNull List<String> getSemanticVersions() {
    File file = Paths.get(SEMVER_VERSION_FILE_NAME).toFile();

    if (file.exists() && file.canRead()) {
      try {
        return FileUtils.readLines(file, Charset.defaultCharset());
      } catch (IOException e) {
        log.info("Unable to read the Semantioc Version file: {}",
            SEMVER_VERSION_FILE_NAME,
            e);
      }
    }

    return Collections.emptyList();
  }

  public String getGitPropertyValue(GitProperty gitProperty) {
    String gitPropertyValue = gitProperties.getProperty(gitProperty.gitPropertyKey);
    Optional<String> optionalGitPropertyValue = Optional.ofNullable(gitPropertyValue);

    return optionalGitPropertyValue.orElse(StringUtils.EMPTY);
  }

  public enum GitProperty {
    GIT_BRANCH("git.branch"),
    GIT_BUILD_HOST("git.build.host"),
    GIT_BUILD_USER_EMAIL("git.build.user.email"),
    GIT_BUILD_USER_NAME("git.build.user.name"),
    GIT_BUILD_VERSION("git.build.version"),
    GIT_CLOSEST_TAG_COMMIT_COUNT("git.closest.tag.commit.count"),
    GIT_CLOSEST_TAG_NAME("git.closest.tag.name"),
    GIT_COMMIT_ID("git.commit.id"),
    GIT_COMMIT_ID_ABBREV("git.commit.id.abbrev"),
    GIT_COMMIT_ID_DESCRIBE("git.commit.id.describe"),
    GIT_COMMIT_MESSAGE_FULL("git.commit.message.full"),
    GIT_COMMIT_MESSAGE_SHORT("git.commit.message.short"),
    GIT_COMMIT_TIME("git.commit.time"),
    GIT_COMMIT_USER_EMAIL("git.commit.user.email"),
    GIT_COMMIT_USER_NAME("git.commit.user.name"),
    GIT_DIRTY("git.dirty"),
    GIT_REMOTE_ORIGIN_URL("git.remote.origin.url"),
    GIT_TAGS("git.tags"),
    GIT_TOTAL_COMMIT_COUNT("git.total.commit.count");

    @Getter
    private final String gitPropertyKey;

    GitProperty(String gitPropertyKey) {
      this.gitPropertyKey = gitPropertyKey;
    }
  }
}
