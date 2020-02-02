package ca.levimiller.smsbridge.data.transformer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.ChatUserType;
import ca.levimiller.smsbridge.data.model.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;


class RoomNameTransformerTest {

  private RoomNameTransformer roomNameTransformer;
  private ChatUser chatNumber;
  private Contact smsContact;

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
    roomNameTransformer = Mappers.getMapper(RoomNameTransformer.class);
  }

  @Test
  void transformHumanReadable() {
    String result = roomNameTransformer.transformHumanReadable(chatNumber, smsContact);
    assertEquals("SMS +smsNumber", result);
  }

  @Test
  void transformEncoded() {
    String result = roomNameTransformer.transformEncoded(chatNumber, smsContact);
    assertEquals("sms-smsNumber", result);
  }
}
