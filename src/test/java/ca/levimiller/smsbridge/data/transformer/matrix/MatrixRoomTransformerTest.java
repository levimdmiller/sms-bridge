package ca.levimiller.smsbridge.data.transformer.matrix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.ChatUserType;
import ca.levimiller.smsbridge.data.transformer.RoomNameTransformer;
import io.github.ma1uta.matrix.client.model.room.CreateRoomRequest;
import java.util.Collections;
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

  private ChatUser chatNumber;
  private Contact smsContact;

  @Autowired
  MatrixRoomTransformerTest(
      MatrixRoomTransformer roomTransformer) {
    this.roomTransformer = roomTransformer;
  }

  @BeforeEach
  void setUp() {
    chatNumber = ChatUser.builder()
        .ownerId("ownerId")
        .userType(ChatUserType.USER)
        .contact(Contact.builder()
            .number("+registrationNumber")
            .build())
        .build();
    smsContact = Contact.builder()
        .number("+smsNumber")
        .build();

    when(roomNameTransformer.transformEncoded(chatNumber, smsContact))
        .thenReturn("alias");
    when(roomNameTransformer.transformHumanReadable(chatNumber, smsContact))
        .thenReturn("Room Name");
  }

  @Test
  void transformUser() {
    chatNumber.setUserType(ChatUserType.USER);
    CreateRoomRequest result = roomTransformer.transform(chatNumber, smsContact);
    assertEquals("trusted_private_chat", result.getPreset());
    assertEquals("alias", result.getRoomAliasName());
    assertEquals("Room Name", result.getName());
    assertEquals("Sms Conversation", result.getTopic());
    assertEquals(Collections.singletonList("ownerId"), result.getInvite());
    assertTrue(result.getDirect());
  }

  @Test
  void transformRoom() {
    chatNumber.setUserType(ChatUserType.ROOM);
    CreateRoomRequest result = roomTransformer.transform(chatNumber, smsContact);
    assertEquals("trusted_private_chat", result.getPreset());
    assertEquals("alias", result.getRoomAliasName());
    assertEquals("Room Name", result.getName());
    assertEquals("Sms Conversation", result.getTopic());
    assertEquals(Collections.singletonList("ownerId"), result.getInvite());
    assertFalse(result.getDirect());
  }
}
