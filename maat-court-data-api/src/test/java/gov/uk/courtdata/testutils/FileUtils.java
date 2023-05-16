package gov.uk.courtdata.testutils;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileUtils {

    private static final String UNABLE_TO_READ_FILE = "Unable to read file with path [%s]";

    @NotNull
    public static String readResourceToString(String path) {
        ClassLoader classLoader = FileUtils.class.getClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream(path);

        if (resourceAsStream == null) {
            throw new IllegalStateException(String.format(UNABLE_TO_READ_FILE, path));
        }

        InputStreamReader streamReader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        StringBuilder fileContents = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                fileContents.append(line);
            }

            bufferedReader.close();
            streamReader.close();
            resourceAsStream.close();
        } catch (IOException e) {
            throw new IllegalStateException(String.format(UNABLE_TO_READ_FILE, path), e);
        }

        return fileContents.toString();
    }
}
