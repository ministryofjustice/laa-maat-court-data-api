package gov.uk.courtdata.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NameUtilsTest {

    private static Stream<Arguments> namesTestData() {
        return Stream.of(
                Arguments.of("carol", "britton", "Carol Britton"),
                Arguments.of(" carol ", " britton ", "Carol Britton"),
                Arguments.of("linda ", " mcCormack", "Linda McCormack"),
                Arguments.of("kim", "", "Kim"),
                Arguments.of("kim", null, "Kim"),
                Arguments.of("lynne", "Lee-Robinson", "Lynne Lee-Robinson"),
                Arguments.of(null, null, ""),
                Arguments.of("", null, ""),
                Arguments.of(null, "", "")
        );
    }

    @ParameterizedTest
    @MethodSource("namesTestData")
    public void givenAFirstAndLastName_theCorrectlyFormattedFullNameIsReturned(String firstName,
                                                                               String lastName,
                                                                               String expectedFullName) {
        String actualFullName = NameUtils.toCapitalisedFullName(firstName, lastName);

        assertEquals(expectedFullName, actualFullName);
    }
}