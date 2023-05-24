package gov.uk.courtdata.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    /**
     *
     * @param inputStringLength The length of the input string.
     * @param expectedOutputLength The expected length of the string after limits applied.
     * @param maxStringLength The maximum string length to enforce.
     */
    @ParameterizedTest
    @CsvSource(value = {"20:10:10", "20:0:0", "20:20:30", "0:0:10"}, delimiter = ':')
    void givenValidInputs_whenApplyMaxLengthLimitToStringIsInvoked_thenTheCorrectLengthStringIsReturned(
            int inputStringLength, int expectedOutputLength, int maxStringLength) {
        String input = "a".repeat(inputStringLength);
        String expectedOutputString = "a".repeat(expectedOutputLength);
        assertEquals(StringUtils.applyMaxLengthLimitToString(input, maxStringLength), expectedOutputString);
    }

    @ParameterizedTest
    @NullSource
    void givenNullInputString_whenApplyMaxLengthLimitToStringIsInvoked_thenInputStringIsReturned(String inputString) {
        assertEquals(StringUtils.applyMaxLengthLimitToString(inputString, 10), inputString);
    }
}