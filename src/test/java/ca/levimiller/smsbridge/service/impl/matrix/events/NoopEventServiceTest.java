package ca.levimiller.smsbridge.service.impl.matrix.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import ca.levimiller.smsbridge.service.MatrixEventService;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import io.github.ma1uta.matrix.event.Event;
import io.github.ma1uta.matrix.event.RoomMessage;
import io.github.ma1uta.matrix.event.content.EventContent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.LoggerFactory;

class NoopEventServiceTest {
  private MatrixEventService<Event<EventContent>, EventContent> eventService;
  private Appender mockAppender;
  //Captor is genericised with ch.qos.logback.classic.spi.LoggingEvent
  private ArgumentCaptor<LoggingEvent> captorLoggingEvent;
  private Logger logger;

  @BeforeEach
  private void setup() {
    mockAppender = mock(Appender.class);
    captorLoggingEvent = ArgumentCaptor.forClass(LoggingEvent.class);
    eventService = new NoopEventService();
    logger = (Logger) LoggerFactory.getLogger(NoopEventService.class);
    logger.addAppender(mockAppender);
  }

  @AfterEach
  public void teardown() {
    logger.detachAppender(mockAppender);
  }

  @Test
  void testProcess() {
    Event event = new RoomMessage();
    eventService.process(event);

    verify(mockAppender).doAppend(captorLoggingEvent.capture());
    LoggingEvent logEvent = captorLoggingEvent.getValue();
    assertEquals(Level.INFO, logEvent.getLevel());
    assertEquals("Event ignored: m.room.message", logEvent.getFormattedMessage());
  }

  @Test
  void testType() {
    assertEquals(MatrixEventService.ANY_TYPE, eventService.getType());
  }
}