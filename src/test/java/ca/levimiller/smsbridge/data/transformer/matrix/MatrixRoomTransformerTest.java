package ca.levimiller.smsbridge.data.transformer.matrix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import ca.levimiller.smsbridge.data.dto.matrix.room.CreateRoomDto;
import ca.levimiller.smsbridge.data.dto.matrix.room.RoomPreset;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.NumberRegistration;
import ca.levimiller.smsbridge.data.model.NumberRegistrationType;
import ca.levimiller.smsbridge.data.transformer.RoomNameTransformer;
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

  private NumberRegistration chatNumber;
  private Contact smsContact;

  @Autowired
  MatrixRoomTransformerTest(
      MatrixRoomTransformer roomTransformer) {
    this.roomTransformer = roomTransformer;
  }

  @BeforeEach
  void setUp() {
    chatNumber = NumberRegistration.builder()
        .ownerId("ownerId")
        .registrationType(NumberRegistrationType.USER)
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
    chatNumber.setRegistrationType(NumberRegistrationType.USER);
    CreateRoomDto result = roomTransformer.transform(chatNumber, smsContact);
    assertEquals(RoomPreset.TRUSTED_PRIVATE, result.getPreset());
    assertEquals("alias", result.getRoomAliasName());
    assertEquals("Room Name", result.getName());
    assertEquals("Sms Conversation", result.getTopic());
    assertEquals(Collections.singletonList("ownerId"), result.getInvite());
    assertTrue(result.isDirect());
  }

  @Test
  void transformRoom() {
    chatNumber.setRegistrationType(NumberRegistrationType.ROOM);
    CreateRoomDto result = roomTransformer.transform(chatNumber, smsContact);
    assertEquals(RoomPreset.TRUSTED_PRIVATE, result.getPreset());
    assertEquals("alias", result.getRoomAliasName());
    assertEquals("Room Name", result.getName());
    assertEquals("Sms Conversation", result.getTopic());
    assertEquals(Collections.singletonList("ownerId"), result.getInvite());
    assertFalse(result.isDirect());
  }
}
