package ca.levimiller.smsbridge.service.impl.matrix.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.levimiller.smsbridge.service.MatrixEventService;
import io.github.ma1uta.matrix.event.Event;
import io.github.ma1uta.matrix.event.RoomCreate;
import io.github.ma1uta.matrix.event.RoomMember;
import io.github.ma1uta.matrix.event.RoomMessage;
import io.github.ma1uta.matrix.event.content.EventContent;
import io.github.ma1uta.matrix.event.message.Text;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CompositeEventServiceTest {

  private MatrixEventService<Event<EventContent>> compositeEventService;
  @Mock
  private MatrixEventService<Event<EventContent>> noopEventService;
  @Mock
  private MatrixEventService<RoomMessage<Text>> messageEventService;
  @Mock
  private MatrixEventService<RoomMember> roomMemberEventService;

  @BeforeEach
  void setUp() {
    when(messageEventService.getType()).thenReturn("m.room.message");
    when(roomMemberEventService.getType()).thenReturn("m.room.member");
    compositeEventService = new CompositeEventService(noopEventService,
        List.of(messageEventService, roomMemberEventService));
  }

  @Test
  void testEventMap() {
    RoomMember roomMemberEvent = new RoomMember();
    compositeEventService.process((Event) roomMemberEvent);
    verify(roomMemberEventService, times(1))
        .process(roomMemberEvent);
  }

  @Test
  void testProcess_RoomMessage() {
    RoomMessage<Text> messageEvent = new RoomMessage<>();
    compositeEventService.process((Event) messageEvent);
    verify(messageEventService, times(1))
        .process(messageEvent);
  }

  @Test
  void testProcess_Unknown() {
    Event roomCreateEvent = new RoomCreate();
    compositeEventService.process(roomCreateEvent);
    verify(noopEventService, times(1))
        .process(roomCreateEvent);
  }

  @Test
  void testType() {
    assertEquals(MatrixEventService.ANY_TYPE, compositeEventService.getType());
  }
}
