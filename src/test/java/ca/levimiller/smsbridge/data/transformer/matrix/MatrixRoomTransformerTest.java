package ca.levimiller.smsbridge.data.transformer.matrix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.ChatUserType;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.transformer.RoomNameTransformer;
import io.github.ma1uta.matrix.client.model.room.CreateRoomRequest;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class MatrixRoomTransformerTest {

  @MockBean
  private RoomNameTransformer roomNameTransformer;
  private final MatrixRoomTransformer roomTransformer;

  private ChatUser chatUser;
  private ChatUser smsUser;

  @Autowired
  MatrixRoomTransformerTest(
      MatrixRoomTransformer roomTransformer) {
    this.roomTransformer = roomTransformer;
  }

  @BeforeEach
  void setUp() {
    chatUser = ChatUser.builder()
        .ownerId("ownerId")
        .userType(ChatUserType.USER)
        .contact(Contact.builder()
            .number("+registrationNumber")
            .build())
        .build();
    smsUser = ChatUser.builder()
        .ownerId("smsOwnerId")
        .userType(ChatUserType.VIRTUAL_USER)
        .contact(Contact.builder()
            .number("+smsNumber")
            .build())
        .build();

    when(roomNameTransformer.transformEncoded(chatUser, smsUser.getContact()))
        .thenReturn("alias");
    when(roomNameTransformer.transformHumanReadable(chatUser, smsUser.getContact()))
        .thenReturn("Room Name");
  }

  @Test
  void transformUser() {
    chatUser.setUserType(ChatUserType.USER);
    CreateRoomRequest result = roomTransformer.transform(chatUser, smsUser);
    assertEquals("trusted_private_chat", result.getPreset());
    assertEquals("alias", result.getRoomAliasName());
    assertEquals("Room Name", result.getName());
    assertEquals("Sms Conversation", result.getTopic());
    assertEquals(Set.of("ownerId", "smsOwnerId"), new HashSet<>(result.getInvite()));
    assertTrue(result.getDirect());
  }

  @Test
  void transformRoom() {
    chatUser.setUserType(ChatUserType.ROOM);
    CreateRoomRequest result = roomTransformer.transform(chatUser, smsUser);
    assertEquals("trusted_private_chat", result.getPreset());
    assertEquals("alias", result.getRoomAliasName());
    assertEquals("Room Name", result.getName());
    assertEquals("Sms Conversation", result.getTopic());
    assertEquals(Set.of("ownerId", "smsOwnerId"), new HashSet<>(result.getInvite()));
    assertFalse(result.getDirect());
  }
}
