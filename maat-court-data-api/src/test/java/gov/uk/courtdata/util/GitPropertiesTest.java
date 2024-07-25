package gov.uk.courtdata.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

class GitPropertiesTest {

  private static final String GIT_PROPERTIES_FILE_NAME = "git.properties";

  @TempDir
  private Path tempDir;

  private File gitPropertiesFile;

  @BeforeEach
  void setUp() {
    gitPropertiesFile = tempDir.resolve(GIT_PROPERTIES_FILE_NAME).toFile();
  }

  @Test
  void shouldLoadGitPropertiesSuccessfully() throws IOException {
    String gitPropertiesFileContents = "git.commit.id=4ddf8ddc55a6348fefea65554bf8316dd4ffdb20";
    FileUtils.writeStringToFile(gitPropertiesFile, gitPropertiesFileContents,
        StandardCharsets.UTF_8);
    Resource resource = new FileSystemResource(gitPropertiesFile);

    GitProperties gitProperties = new GitProperties(resource);

    String actualGitCommitIdValue = gitProperties.getValueOf(GitProperty.GIT_COMMIT_ID);

    assertEquals("4ddf8ddc55a6348fefea65554bf8316dd4ffdb20", actualGitCommitIdValue);
  }

  @Test
  void shouldHandleIOExceptionGracefully() throws IOException {
    Files.createFile(gitPropertiesFile.toPath());
    assertTrue(gitPropertiesFile.setReadable(false), "Failed precondition");
    Resource resource = new FileSystemResource(gitPropertiesFile);

    GitProperties gitProperties = new GitProperties(resource);

    String actualGitCommitIdValue = gitProperties.getValueOf(GitProperty.GIT_COMMIT_ID);

    assertEquals(StringUtils.EMPTY, actualGitCommitIdValue);
  }

  @Test
  void shouldReturnEmptyStringForMissingProperty() throws IOException {
    String content = "git.commit.date=2022-01-01";
    FileUtils.writeStringToFile(gitPropertiesFile, content, StandardCharsets.UTF_8);
    Resource resource = new FileSystemResource(gitPropertiesFile);

    GitProperties gitProperties = new GitProperties(resource);

    String actualGitCommitIdValue = gitProperties.getValueOf(GitProperty.GIT_COMMIT_ID);

    assertEquals(StringUtils.EMPTY, actualGitCommitIdValue);
  }

  @Test
  void shouldHandleMissingFileGracefully() {
    Resource resource = new FileSystemResource(gitPropertiesFile);
    GitProperties gitProperties = new GitProperties(resource);

    String actualGitCommitIdValue = gitProperties.getValueOf(GitProperty.GIT_COMMIT_ID);

    assertEquals(StringUtils.EMPTY, actualGitCommitIdValue);
  }
}
