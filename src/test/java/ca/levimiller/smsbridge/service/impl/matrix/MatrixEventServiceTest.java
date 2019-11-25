package ca.levimiller.smsbridge.service.impl.matrix;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import ca.levimiller.smsbridge.data.dto.matrix.EventDto;
import ca.levimiller.smsbridge.data.dto.matrix.EventType;
import ca.levimiller.smsbridge.data.dto.matrix.content.EventContent;
import ca.levimiller.smsbridge.util.UuidSource;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class MatrixEventServiceTest {

  private final MatrixEventService eventService;
  @MockBean
  private UuidSource uuidSource;
  @MockBean
  @Qualifier("matrixTemplate")
  private RestTemplate restTemplate;

  private UUID uuid;
  private EventDto eventDto;
  private EventContent content;

  @Autowired
  MatrixEventServiceTest(
      MatrixEventService eventService) {
    this.eventService = eventService;
  }

  @BeforeEach
  void setUp() {
    uuid = UUID.fromString("0a2afa26-b13e-4b70-9fb2-f870722a6e76");
    when(uuidSource.newUuid()).thenReturn(uuid);

    content = new EventContent();
    eventDto = EventDto.builder()
        .roomId("roomId")
        .type(EventType.ROOM_MESSAGE)
        .content(content)
        .build();
  }

  @Test
  void sendRoomEvent_Message() {
    eventDto.setStateKey("");
    eventService.sendRoomEvent(eventDto);

    verify(restTemplate, times(1))
        .put("/rooms/roomId/send/m.room.message/0a2afa26-b13e-4b70-9fb2-f870722a6e76", content);
  }

  @Test
  void sendRoomEvent_State() {
    eventDto.setStateKey("stateKey");
    eventService.sendRoomEvent(eventDto);

    verify(restTemplate, times(1))
        .put("/rooms/roomId/state/m.room.message/stateKey", content);
  }
}
