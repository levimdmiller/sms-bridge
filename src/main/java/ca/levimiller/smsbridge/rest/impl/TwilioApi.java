package ca.levimiller.smsbridge.rest.impl;

import ca.levimiller.smsbridge.data.db.MessageRepository;
import ca.levimiller.smsbridge.data.dto.TwilioSmsDto;
import ca.levimiller.smsbridge.data.dto.TwilioVoiceDto;
import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.data.transformer.twilio.MessageTransformer;
import ca.levimiller.smsbridge.data.transformer.twilio.VoiceCallTransformer;
import ca.levimiller.smsbridge.rest.TwilioController;
import ca.levimiller.smsbridge.service.ChatService;
import ca.levimiller.smsbridge.service.VoiceService;
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Dial;
import com.twilio.twiml.voice.Say;
import javax.inject.Inject;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TwilioApi implements TwilioController {

  private final ChatService chatService;
  private final VoiceService voiceService;
  private final MessageTransformer messageTransformer;
  private final VoiceCallTransformer voiceCallTransformer;
  private final MessageRepository messageRepository;

  @Inject
  public TwilioApi(
      @Qualifier("matrixChatService")
          ChatService chatService,
      VoiceService voiceService,
      MessageTransformer messageTransformer,
      VoiceCallTransformer voiceCallTransformer,
      MessageRepository messageRepository) {
    this.chatService = chatService;
    this.voiceService = voiceService;
    this.messageTransformer = messageTransformer;
    this.voiceCallTransformer = voiceCallTransformer;
    this.messageRepository = messageRepository;
  }

  @Override
  public void createSms(TwilioSmsDto sms) {
    log.debug("Received sms: {}", sms);
    Message message = messageTransformer.transform(sms);
    messageRepository.save(message);
    chatService.sendMessage(message);
  }

  @Override
  public void voice(@Valid TwilioVoiceDto voiceCall) {
    log.debug("Received voice: {}", voiceCall);

    Dial dial = new Dial.Builder("415-123-4567").build();
    Say say = new Say.Builder("Goodbye").build();
    VoiceResponse response = new VoiceResponse.Builder().dial(dial)
        .say(say).build();

    try {
      System.out.println(response.toXml());
    } catch (TwiMLException e) {
      log.error("Error generating response TwiML: ", e);
    }
    voiceService.startCall(voiceCallTransformer.transform(voiceCall));
  }
}
