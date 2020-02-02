package ca.levimiller.smsbridge.service.impl.matrix;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.levimiller.smsbridge.data.db.ChatUserRepository;
import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.error.NotFoundException;
import ca.levimiller.smsbridge.service.ChatService;
import ca.levimiller.smsbridge.service.ChatServiceTest;
import ca.levimiller.smsbridge.service.RoomService;
import ca.levimiller.smsbridge.service.UserService;
import io.github.ma1uta.matrix.client.AppServiceClient;
import io.github.ma1uta.matrix.client.methods.EventMethods;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class MatrixChatServiceTest extends ChatServiceTest {
  @MockBean
  private ChatUserRepository chatUserRepository;
  @MockBean
  private RoomService roomService;
  @MockBean
  private UserService userService;
  @MockBean
  private AppServiceClient matrixClient;
  @Mock
  private AppServiceClient userClient;
  @Mock
  private EventMethods eventMethods;

  private Message message;
  private Contact fromContact;
  private Contact toContact;
  private ChatUser toRegistration;

  @Autowired
  MatrixChatServiceTest(@Qualifier("matrixChatService") ChatService chatService) {
    super(chatService);
  }

  @BeforeEach
  void setUp() {
    fromContact = Contact.builder().number("1235").build();
    toContact = Contact.builder().number("67890").build();
    toRegistration = new ChatUser();
    message = Message.builder()
        .fromContact(fromContact)
        .toContact(toContact)
        .body("body")
        .build();
    when(userService.getUser(fromContact)).thenReturn("user-id");
    when(roomService.getRoom(toRegistration, fromContact))
        .thenReturn("room-id");

    when(matrixClient.userId("user-id")).thenReturn(userClient);
    when(userClient.event()).thenReturn(eventMethods);
  }

  @Test
  void sendMessage() {
    Optional<ChatUser> maybeRegistration = Optional.of(toRegistration);
    when(chatUserRepository.findDistinctByContact(toContact))
        .thenReturn(maybeRegistration);
    chatService.sendMessage(message);
    verify(eventMethods, times(1)).sendMessage("room-id", "body");
  }

  @Test
  void sendMessage_Error() {
    when(chatUserRepository.findDistinctByContact(toContact))
        .thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> chatService.sendMessage(message));
  }
}
