package ca.levimiller.smsbridge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.Message;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class ChatServiceTest {
  protected final ChatService chatService;
  private Message message;

  protected ChatServiceTest(ChatService chatService) {
    this.chatService = chatService;
  }

  @BeforeEach
  void setup() {
    message = Message.builder()
        .toContact(Contact.builder().number("12345").build())
        .fromContact(Contact.builder().number("67890").build())
        .build();
  }

  @Test
  void sendMessage_NullOrigin() {
    message.setFromContact(null);

    ConstraintViolationException result = assertThrows(ConstraintViolationException.class,
        () -> chatService.sendMessage(message));
    assertEquals("sendMessage.message.fromContact: must not be null", result.getMessage());
  }

  @Test
  void sendMessage_NullDestination() {
    message.setToContact(null);

    ConstraintViolationException result = assertThrows(ConstraintViolationException.class,
        () -> chatService.sendMessage(message));
    assertEquals("sendMessage.message.toContact: must not be null", result.getMessage());
  }
}
