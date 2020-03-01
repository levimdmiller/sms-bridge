package ca.levimiller.smsbridge.rest.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.levimiller.smsbridge.data.db.MessageRepository;
import ca.levimiller.smsbridge.data.dto.TwilioSmsDto;
import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.data.transformer.twilio.MessageTransformer;
import ca.levimiller.smsbridge.rest.TwilioController;
import ca.levimiller.smsbridge.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class TwilioApiTest {
  private final TwilioController twilioController;

  @MockBean
  @Qualifier("matrixChatService")
  private ChatService chatService;
  @MockBean
  private MessageTransformer messageTransformer;
  @MockBean
  private MessageRepository messageRepository;

  private TwilioSmsDto smsDto;
  private Message message;

  @Autowired
  TwilioApiTest(TwilioController twilioController) {
    this.twilioController = twilioController;
  }

  @BeforeEach
  void setUp() {
    smsDto = new TwilioSmsDto();
    message = new Message();
    when(messageTransformer.transform(smsDto)).thenReturn(message);
  }

  @Test
  void testCreateSms() {
    twilioController.createSms(smsDto);
    verify(messageRepository).save(message);
    verify(chatService).sendMessage(message);
  }
}
