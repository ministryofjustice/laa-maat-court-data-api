package gov.uk.courtdata.util;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class GitPropertyTest {

    @ParameterizedTest
    @MethodSource("allGitPropertyValues")
    void assertThatAllGitProperty_havePopulatedKey(GitProperty gitProperty) {
        String gitPropertyKey = gitProperty.getGitPropertyKey();

        assertThat(gitPropertyKey).isNotBlank();
    }

    private static Stream<Arguments> allGitPropertyValues() {
        return Arrays.stream(GitProperty.values()).map(Arguments::of);
    }
}