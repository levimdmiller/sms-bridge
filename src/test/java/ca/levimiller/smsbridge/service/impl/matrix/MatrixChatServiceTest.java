package ca.levimiller.smsbridge.service.impl.matrix;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.levimiller.smsbridge.data.db.NumberRegistryRepository;
import ca.levimiller.smsbridge.data.dto.matrix.EventDto;
import ca.levimiller.smsbridge.data.dto.matrix.EventType;
import ca.levimiller.smsbridge.data.dto.matrix.content.TextContent;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.data.model.NumberRegistration;
import ca.levimiller.smsbridge.error.NotFoundException;
import ca.levimiller.smsbridge.service.ChatService;
import ca.levimiller.smsbridge.service.RoomService;
import ca.levimiller.smsbridge.util.UuidSource;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class MatrixChatServiceTest {
  @MockBean
  private NumberRegistryRepository numberRegistryRepository;
  @MockBean
  private MatrixEventService eventService;
  @MockBean
  private RoomService roomService;
  @MockBean
  private UuidSource uuidSource;
  private final ChatService chatService;

  private Message message;
  private Contact fromContact;
  private Contact toContact;
  private NumberRegistration toRegistration;
  private UUID uuid;

  @Autowired
  MatrixChatServiceTest(ChatService chatService) {
    this.chatService = chatService;
  }

  @BeforeEach
  void setUp() {
    fromContact = new Contact();
    toContact = new Contact();
    toRegistration = new NumberRegistration();
    message = Message.builder()
        .fromContact(fromContact)
        .toContact(toContact)
        .body("body")
        .build();
    when(roomService.getRoom(toRegistration, fromContact))
        .thenReturn("room-id");
    uuid = UUID.fromString("0a2afa26-b13e-4b70-9fb2-f870722a6e76");
    when(uuidSource.newUuid()).thenReturn(uuid);
  }

  @Test
  void sendMessage() {
    Optional<NumberRegistration> maybeRegistration = Optional.of(toRegistration);
    when(numberRegistryRepository.findDistinctByContact(toContact))
        .thenReturn(maybeRegistration);
    chatService.sendMessage(message);
    verify(eventService, times(1)).sendRoomEvent(EventDto.builder()
        .eventId(uuid.toString())
        .roomId("room-id")
        .type(EventType.ROOM_MESSAGE)
        .content(TextContent.builder()
            .body(message.getBody())
            .build())
        .build());
  }

  @Test
  void sendMessage_Error() {
    when(numberRegistryRepository.findDistinctByContact(toContact))
        .thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> chatService.sendMessage(message));
  }
}
