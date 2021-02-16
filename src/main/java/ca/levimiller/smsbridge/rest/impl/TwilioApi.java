package ca.levimiller.smsbridge.rest.impl;

import ca.levimiller.smsbridge.data.dto.TwilioSmsDto;
import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.data.transformer.twilio.MessageTransformer;
import ca.levimiller.smsbridge.rest.TwilioController;
import ca.levimiller.smsbridge.service.ChatService;
import ca.levimiller.smsbridge.service.MessageService;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TwilioApi implements TwilioController {

  private final ChatService chatService;
  private final MessageTransformer messageTransformer;
  private final MessageService messageService;

  @Inject
  public TwilioApi(
      @Qualifier("matrixChatService")
          ChatService chatService,
      MessageTransformer messageTransformer,
      MessageService messageService) {
    this.chatService = chatService;
    this.messageTransformer = messageTransformer;
    this.messageService = messageService;
  }

  @Override
  public void createSms(TwilioSmsDto sms) {
    log.debug("Received sms: {}", sms);
    Message message = messageTransformer.transform(sms);
    if (message.getMedia() != null) {
      message.getMedia().forEach(media -> media.setMessage(message));
    }
    messageService.save(message);
    chatService.sendMessage(message);
  }
}
