package gov.uk.courtdata.util;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

@Slf4j
class GitProperties {

  private static final String GIT_PROPERTIES_FILE_NAME = "git.properties";

  private final Properties properties;

  GitProperties() {
    this(new ClassPathResource(GIT_PROPERTIES_FILE_NAME));
  }

  GitProperties(Resource gitPropertiesResource) {
    Properties loadedGitProperties = null;
    try {
      loadedGitProperties = PropertiesLoaderUtils.loadProperties(gitPropertiesResource);
    } catch (IOException e) {
      log.info("Unable to load git properties file [{}] due to: {}", gitPropertiesResource,
          e.getMessage(), e);
    }
    if (Objects.isNull(loadedGitProperties)) {
      properties = new Properties();
    } else {
      properties = loadedGitProperties;
    }
  }

  String getValueOf(GitProperty gitProperty) {
    String gitPropertyKey = gitProperty.getGitPropertyKey();
    return properties.getProperty(gitPropertyKey, StringUtils.EMPTY);
  }
}
