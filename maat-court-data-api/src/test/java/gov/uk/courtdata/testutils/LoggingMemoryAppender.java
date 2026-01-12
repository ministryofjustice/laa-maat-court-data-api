package gov.uk.courtdata.testutils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;

/**
 * In memory slf4j appender<br/> Convenient appender to be able to check slf4j invocations<br/>
 * Copied from <a
 * href="https://github.com/ministryofjustice/laa-crime-commons/blob/f788ee5ecaa71905ec7747972019cd3af8ac60a6/crime-commons-spring-boot-starter-rest-client/src/test/java/uk/gov/justice/laa/crime/commons/util/MemoryAppender.java">laa-crime-commons
 * - MemoryAppender.java</a>
 */
public class LoggingMemoryAppender extends ListAppender<ILoggingEvent> {

    public void reset() {
        this.list.clear();
    }

    public void assertContains(String message, Level level) {
        Optional<ILoggingEvent> matchingLoggingEvent = this.list.stream()
                .filter(loggingEvent -> loggingEvent.getLevel().equals(level))
                .filter(event -> message.matches(event.getFormattedMessage()))
                .findFirst();

        assertTrue(matchingLoggingEvent.isPresent(), createFailureMessage(message, level));
    }

    private String createFailureMessage(String message, Level level) {
        String allLogEvents = this.list.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));

        return String.format(
                "Failed to find a logging event with message matching [%s] and Level %s in [%s]",
                message,
                level,
                allLogEvents);
    }

    public int countEventsForLogger(String loggerName) {
        return (int) this.list.stream()
                .filter(event -> event.getLoggerName().contains(loggerName)).count();
    }

    public List<ILoggingEvent> search(String string) {
        return this.list.stream()
                .filter(event -> event.toString().contains(string))
                .collect(Collectors.toList());
    }

    public List<ILoggingEvent> search(String string, Level level) {
        return this.list.stream()
                .filter(event -> event.toString().contains(string)
                        && event.getLevel().equals(level))
                .collect(Collectors.toList());
    }

    public int getSize() {
        return this.list.size();
    }

    public List<ILoggingEvent> getLoggedEvents() {
        return Collections.unmodifiableList(this.list);
    }

    public void addAppenderTo(Class<?> clazz) {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(
                clazz);
        logger.setLevel(Level.ALL);
        logger.addAppender(this);
        setContext((LoggerContext) LoggerFactory.getILoggerFactory());
    }
}

