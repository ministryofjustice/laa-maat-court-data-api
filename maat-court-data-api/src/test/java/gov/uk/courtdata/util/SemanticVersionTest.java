package gov.uk.courtdata.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class SemanticVersionTest {

    private static final String VERSION_FILE_NAME = "version.txt";

    @TempDir
    private Path tempDir;

    private File versionFile;

    @BeforeEach
    void setUp() {
        versionFile = tempDir.resolve(VERSION_FILE_NAME).toFile();
    }

    @Test
    void shouldLoadSemanticVersionsFromFile() throws IOException {
        String fileContent = """
                1.0.0
                1.1.0
                1.2.0""";
        FileUtils.writeStringToFile(versionFile, fileContent, Charset.defaultCharset());

        assertThat(createSemanticVersion().asText()).isEqualTo("1.0.0|1.1.0|1.2.0");
    }

    @Test
    void shouldHandleMissingFileGracefully() {
        assertThat(createSemanticVersion().asText()).isEmpty();
    }

    @Test
    void shouldHandleEmptyFileGracefully() throws IOException {
        Files.createFile(versionFile.toPath());

        assertThat(createSemanticVersion().asText()).isEmpty();
    }

    @Test
    void shouldHandleUnreadableFileGracefully() throws IOException {
        Files.createFile(versionFile.toPath());
        assertThat(versionFile.setReadable(false))
                .withFailMessage("failed precondition")
                .isTrue();

        assertThat(createSemanticVersion().asText()).isEmpty();
    }

    private @NotNull SemanticVersion createSemanticVersion() {
        return new SemanticVersion() {
            @Override
            String getPathToSemverVersionFile() {
                return versionFile.getAbsolutePath();
            }
        };
    }
}
