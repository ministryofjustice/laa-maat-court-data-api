package gov.uk.courtdata.enums;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.MDC;

class LoggingDataTest {

    @BeforeEach
    void setUp() {
        MDC.clear();
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @ParameterizedTest
    @MethodSource("allLoggingDataValues")
    void putInMDC_NullInteger(LoggingData loggingData) {
        final Integer value = null;
        assertNull(MDC.get(loggingData.getKey()), "Failed precondition");

        loggingData.putInMDC(value);

        assertNull(MDC.get(loggingData.getKey()));
    }

    @ParameterizedTest
    @MethodSource("allLoggingDataValues")
    void putInMDC_NullString(LoggingData loggingData) {
        final String expectedValue = null;
        assertNull(MDC.get(loggingData.getKey()), "Failed precondition");

        loggingData.putInMDC(expectedValue);

        assertNull(MDC.get(loggingData.getKey()));
    }

    @ParameterizedTest
    @MethodSource("allLoggingDataValues")
    void putInMDC_NonNullInteger(LoggingData loggingData) {
        final Integer value = 763456;
        assertNull(MDC.get(loggingData.getKey()), "Failed precondition");

        loggingData.putInMDC(value);

        String expectedStringValue = String.valueOf(value);
        assertEquals(expectedStringValue, MDC.get(loggingData.getKey()));
    }

    @ParameterizedTest
    @MethodSource("allLoggingDataValues")
    void putInMDC_NonNullString(LoggingData loggingData) {
        final String expectedValue = "48e60e52-70f9-415d-8c57-c25a16419a7c";
        assertNull(MDC.get(loggingData.getKey()), "Failed precondition");

        loggingData.putInMDC(expectedValue);

        assertEquals(expectedValue, MDC.get(loggingData.getKey()));
    }

    @ParameterizedTest
    @MethodSource("allLoggingDataValues")
    void getKeyIsNotBlank(LoggingData loggingData) {
        assertThat(loggingData.getKey())
                .as("Expected loggingData %s to have non-blank or null key".formatted(loggingData))
                .isNotBlank();
    }

    @ParameterizedTest
    @MethodSource("allLoggingDataValues")
    void getValueFromMDC_NullValue(LoggingData loggingData) {
        final String expectedValue = StringUtils.EMPTY;
        assertNull(MDC.get(loggingData.getKey()), "Failed precondition");

        String valueFromMDC = loggingData.getValueFromMDC();

        assertEquals(expectedValue, valueFromMDC);
    }

    @ParameterizedTest
    @MethodSource("allLoggingDataValues")
    void getValueFromMDC_NonNullValue(LoggingData loggingData) {
        final String expectedValue = "48e60e52-70f9-415d-8c57-c25a16419a7c";
        MDC.put(loggingData.getKey(), expectedValue);

        String valueFromMDC = loggingData.getValueFromMDC();

        assertEquals(expectedValue, valueFromMDC);
    }

    private static Stream<Arguments> allLoggingDataValues() {
        return Arrays.stream(LoggingData.values()).map(Arguments::of);
    }
}