package gov.uk.courtdata.util;

import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GitPropertiesFile {

  private static final String GIT_PROPERTIES_FILE_NAME = "git.properties";

  private final Properties properties;

  public GitPropertiesFile() {
    Resource gitPropertiesResource = getGitPropertiesResource();

    Properties loadedGitProperties = null;
    try {
      loadedGitProperties = PropertiesLoaderUtils.loadProperties(gitPropertiesResource);
    } catch (IOException e) {
      log.info("Unable to load git properties [{}] due to: [{}]", gitPropertiesResource,
          e.getMessage(), e);
    }
    if (Objects.isNull(loadedGitProperties)) {
      properties = new Properties();
    } else {
      properties = loadedGitProperties;
    }
  }

  // Exists to allow overriding for unit testing
  Resource getGitPropertiesResource() {
    return new ClassPathResource(GIT_PROPERTIES_FILE_NAME);
  }

  @NotNull
  public String getValueOf(GitProperty gitProperty) {
    String gitPropertyKey = gitProperty.getGitPropertyKey();
    return properties.getProperty(gitPropertyKey, StringUtils.EMPTY);
  }
}
