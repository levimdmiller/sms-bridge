package ca.levimiller.smsbridge.service.impl.twilio;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.service.ChatService;
import ca.levimiller.smsbridge.service.ChatServiceTest;
import ca.levimiller.smsbridge.twilio.MessageCreatorFactory;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class TwilioChatServiceTest extends ChatServiceTest {

  @MockBean
  private MessageCreatorFactory messageCreatorFactory;
  private MessageCreator messageCreator;

  private Message message;
  private PhoneNumber to;
  private PhoneNumber from;
  private String body;

  @Autowired
  TwilioChatServiceTest(@Qualifier("twilioChatService") ChatService twilioChatService) {
    super(twilioChatService);
  }

  @BeforeEach
  void setUp() {
    to = new PhoneNumber("12345");
    from = new PhoneNumber("67890");
    body = "body";
    message = Message.builder()
        .toContact(Contact.builder().number("12345").build())
        .fromContact(Contact.builder().number("67890").build())
        .body("body")
        .build();

    messageCreator = mock(MessageCreator.class);
    when(messageCreatorFactory.getCreator(eq(to), eq(from), eq(body)))
        .thenReturn(messageCreator);
  }

  @Test
  void sendMessage() {
    chatService.sendMessage(message);

    verify(messageCreatorFactory, times(1))
        .getCreator(eq(to), eq(from), eq(body));
    verify(messageCreator, times(1)).create();
  }
}
