package gov.uk.courtdata.enums;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(MDC.get(loggingData.getKey())).as("Failed precondition").isNull();

        loggingData.putInMDC(value);

        assertThat(MDC.get(loggingData.getKey())).isNull();
    }

    @ParameterizedTest
    @MethodSource("allLoggingDataValues")
    void putInMDC_NullString(LoggingData loggingData) {
        final String expectedValue = null;
        assertThat(MDC.get(loggingData.getKey())).as("Failed precondition").isNull();

        loggingData.putInMDC(expectedValue);

        assertThat(MDC.get(loggingData.getKey())).isNull();
    }

    @ParameterizedTest
    @MethodSource("allLoggingDataValues")
    void putInMDC_NonNullInteger(LoggingData loggingData) {
        final Integer value = 763456;
        assertThat(MDC.get(loggingData.getKey())).as("Failed precondition").isNull();

        loggingData.putInMDC(value);

        String expectedStringValue = String.valueOf(value);
        assertThat(MDC.get(loggingData.getKey())).isEqualTo(expectedStringValue);
    }

    @ParameterizedTest
    @MethodSource("allLoggingDataValues")
    void putInMDC_NonNullString(LoggingData loggingData) {
        final String expectedValue = "48e60e52-70f9-415d-8c57-c25a16419a7c";
        assertThat(MDC.get(loggingData.getKey())).as("Failed precondition").isNull();

        loggingData.putInMDC(expectedValue);

        assertThat(MDC.get(loggingData.getKey())).isEqualTo(expectedValue);
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
        assertThat(MDC.get(loggingData.getKey())).as("Failed precondition").isNull();

        String valueFromMDC = loggingData.getValueFromMDC();

        assertThat(valueFromMDC).isEqualTo(expectedValue);
    }

    @ParameterizedTest
    @MethodSource("allLoggingDataValues")
    void getValueFromMDC_NonNullValue(LoggingData loggingData) {
        final String expectedValue = "48e60e52-70f9-415d-8c57-c25a16419a7c";
        MDC.put(loggingData.getKey(), expectedValue);

        String valueFromMDC = loggingData.getValueFromMDC();
        assertThat(valueFromMDC).isEqualTo(expectedValue);
    }

    private static Stream<Arguments> allLoggingDataValues() {
        return Arrays.stream(LoggingData.values()).map(Arguments::of);
    }
}
