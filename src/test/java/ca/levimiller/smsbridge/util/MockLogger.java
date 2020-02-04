package ca.levimiller.smsbridge.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.Appender;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;

public class MockLogger {

  private final Appender mockAppender;
  //Captor is genericised with ch.qos.logback.classic.spi.LoggingEvent
  private final ArgumentCaptor<LoggingEvent> captorLoggingEvent;
  private final Logger logger;

  public MockLogger(Class<?> loggedClass) {
    mockAppender = mock(Appender.class);
    captorLoggingEvent = ArgumentCaptor.forClass(LoggingEvent.class);
    logger = (Logger) LoggerFactory.getLogger(loggedClass);
    logger.addAppender(mockAppender);
  }

  /**
   * Verifies that a log entry with the given level and message was submitted.
   *
   * @param expectedLevel - log level
   * @param expectedMessage - formatted log message
   */
  public void verify(Level expectedLevel, String expectedMessage) {
    Mockito.verify(mockAppender).doAppend(captorLoggingEvent.capture());
    LoggingEvent logEvent = captorLoggingEvent.getValue();
    assertEquals(expectedLevel, logEvent.getLevel());
    assertEquals(expectedMessage, logEvent.getFormattedMessage());
  }

  /**
   * Verifies that a log entry with the given level, message, and throwable was submitted.
   *
   * @param expectedLevel - log level
   * @param expectedMessage - formatted log message
   * @param expectedCause - passed in throwable
   */
  public void verify(Level expectedLevel, String expectedMessage, Throwable expectedCause) {
    Mockito.verify(mockAppender).doAppend(captorLoggingEvent.capture());
    LoggingEvent logEvent = captorLoggingEvent.getValue();
    assertEquals(expectedLevel, logEvent.getLevel());
    assertEquals(expectedMessage, logEvent.getFormattedMessage());
    IThrowableProxy proxy = logEvent.getThrowableProxy();
    assertEquals(expectedCause, ((ThrowableProxy) proxy).getThrowable());
  }

  public void teardown() {
    logger.detachAppender(mockAppender);
  }
}
