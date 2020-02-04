package ca.levimiller.smsbridge.service.impl.matrix.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.levimiller.smsbridge.service.MatrixEventService;
import ca.levimiller.smsbridge.util.MockLogger;
import ch.qos.logback.classic.Level;
import io.github.ma1uta.matrix.event.Event;
import io.github.ma1uta.matrix.event.RoomMessage;
import io.github.ma1uta.matrix.event.content.EventContent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NoopEventServiceTest {
  private MatrixEventService<Event<EventContent>> eventService;
  private MockLogger mockLogger;

  @BeforeEach
  private void setup() {
    eventService = new NoopEventService();
    mockLogger = new MockLogger(NoopEventService.class);
  }

  @AfterEach
  public void teardown() {
    mockLogger.teardown();
  }

  @Test
  void testProcess() {
    Event event = new RoomMessage();
    eventService.process(event);

    mockLogger.verify(Level.INFO, "Event ignored: m.room.message");
  }

  @Test
  void testType() {
    assertEquals(MatrixEventService.ANY_TYPE, eventService.getType());
  }
}
