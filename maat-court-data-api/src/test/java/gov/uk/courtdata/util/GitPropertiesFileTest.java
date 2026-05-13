package gov.uk.courtdata.util;

import static org.assertj.core.api.Assertions.assertThat;

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

class GitPropertiesFileTest {

    private static final String GIT_PROPERTIES_FILE_NAME = "git.properties";

    @TempDir
    private Path tempDir;

    private File tempFile;

    @BeforeEach
    void setUp() {
        tempFile = tempDir.resolve(GIT_PROPERTIES_FILE_NAME).toFile();
    }

    @Test
    void shouldLoadGitPropertiesSuccessfully() throws IOException {
        String gitPropertiesFileContents = "git.commit.id=4ddf8ddc55a6348fefea65554bf8316dd4ffdb20";
        FileUtils.writeStringToFile(this.tempFile, gitPropertiesFileContents, StandardCharsets.UTF_8);
        Resource resource = new FileSystemResource(this.tempFile);

        GitPropertiesFile gitPropertiesFile = createGitPropertiesFileWith(resource);

        String actualGitCommitIdValue = gitPropertiesFile.getValueOf(GitProperty.GIT_COMMIT_ID);

        assertThat(actualGitCommitIdValue).isEqualTo("4ddf8ddc55a6348fefea65554bf8316dd4ffdb20");
    }

    @Test
    void shouldHandleIOExceptionGracefully() throws IOException {
        Files.createFile(this.tempFile.toPath());
        assertThat(this.tempFile.setReadable(false))
                .withFailMessage("Failed precondition")
                .isTrue();
        Resource resource = new FileSystemResource(this.tempFile);

        GitPropertiesFile gitPropertiesFile = createGitPropertiesFileWith(resource);

        String actualGitCommitIdValue = gitPropertiesFile.getValueOf(GitProperty.GIT_COMMIT_ID);

        assertThat(actualGitCommitIdValue).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    void shouldReturnEmptyStringForMissingProperty() throws IOException {
        String content = "git.commit.date=2022-01-01";
        FileUtils.writeStringToFile(this.tempFile, content, StandardCharsets.UTF_8);
        Resource resource = new FileSystemResource(this.tempFile);

        GitPropertiesFile gitPropertiesFile = createGitPropertiesFileWith(resource);

        String actualGitCommitIdValue = gitPropertiesFile.getValueOf(GitProperty.GIT_COMMIT_ID);

        assertThat(actualGitCommitIdValue).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    void shouldHandleMissingFileGracefully() {
        Resource resource = new FileSystemResource(this.tempFile);
        GitPropertiesFile gitPropertiesFile = createGitPropertiesFileWith(resource);

        String actualGitCommitIdValue = gitPropertiesFile.getValueOf(GitProperty.GIT_COMMIT_ID);

        assertThat(actualGitCommitIdValue).isEqualTo(StringUtils.EMPTY);
    }

    private GitPropertiesFile createGitPropertiesFileWith(Resource resource) {
        return new GitPropertiesFile() {
            @Override
            Resource getGitPropertiesResource() {
                return resource;
            }
        };
    }
}
