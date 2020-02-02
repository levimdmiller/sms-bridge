package ca.levimiller.smsbridge.data.transformer.matrix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import ca.levimiller.smsbridge.data.db.ChatUserRepository;
import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.error.TransformationException;
import ca.levimiller.smsbridge.service.RoomService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class MatrixContactTransformerTest {

  private final MatrixContactTransformer matrixContactTransformer;
  @MockBean
  private ChatUserRepository chatUserRepository;
  @MockBean
  private RoomService roomService;

  private String roomId;
  private String sender;
  private Contact contact;

  @Autowired
  MatrixContactTransformerTest(
      MatrixContactTransformer matrixContactTransformer) {
    this.matrixContactTransformer = matrixContactTransformer;
  }

  @BeforeEach
  void setUp() {
    roomId = "roomId";
    sender = "sender";
    contact = new Contact();
  }

  @Test
  void transformTo() {
    when(roomService.getNumber(roomId)).thenReturn(contact);
    Contact result = matrixContactTransformer.transformTo(roomId);
    assertEquals(contact, result);
  }

  @Test
  void transformFrom() throws TransformationException {
    ChatUser chatUser = ChatUser.builder()
        .ownerId(sender)
        .contact(contact)
        .build();
    when(chatUserRepository.findDistinctByOwnerId(sender))
        .thenReturn(Optional.of(chatUser));

    Contact result = matrixContactTransformer.transformFrom(sender);
    assertEquals(contact, result);
  }

  @Test
  void transformFrom_NoLink() {
    when(chatUserRepository.findDistinctByOwnerId(sender))
        .thenReturn(Optional.empty());
    TransformationException thrown = assertThrows(TransformationException.class,
        () -> matrixContactTransformer.transformFrom(sender));
    assertEquals("Message sender isn't linked to a number: sender", thrown.getMessage());
  }
}
