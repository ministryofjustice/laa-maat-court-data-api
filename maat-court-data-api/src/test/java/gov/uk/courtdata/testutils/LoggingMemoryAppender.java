package gov.uk.courtdata.testutils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * In memory slf4j appender<br/>
 * Convenient appender to be able to check slf4j invocations<br/>
 * Copied from <a href="https://github.com/ministryofjustice/laa-crime-commons/blob/f788ee5ecaa71905ec7747972019cd3af8ac60a6/crime-commons-spring-boot-starter-rest-client/src/test/java/uk/gov/justice/laa/crime/commons/util/MemoryAppender.java">laa-crime-commons - MemoryAppender.java</a>
 */
public class LoggingMemoryAppender extends ListAppender<ILoggingEvent> {
  public void reset() {
    this.list.clear();
  }

  public boolean contains(String string, Level level) {
    return this.list.stream()
        .anyMatch(event -> event.toString().contains(string)
            && event.getLevel().equals(level));
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
}

