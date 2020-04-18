package ca.levimiller.smsbridge.rest.impl;

import ca.levimiller.smsbridge.data.db.MessageRepository;
import ca.levimiller.smsbridge.data.db.VoiceCallRepository;
import ca.levimiller.smsbridge.data.dto.TwilioSmsDto;
import ca.levimiller.smsbridge.data.dto.TwilioTokenDto;
import ca.levimiller.smsbridge.data.dto.TwilioVoiceDto;
import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.data.transformer.twilio.MessageTransformer;
import ca.levimiller.smsbridge.data.transformer.twilio.VoiceCallTransformer;
import ca.levimiller.smsbridge.rest.TwilioController;
import ca.levimiller.smsbridge.service.ChatService;
import ca.levimiller.smsbridge.service.impl.twilio.TwilioWebsocketClient;
import ca.levimiller.smsbridge.service.impl.twilio.TwilioWebsocketClientFactory;
import ca.levimiller.smsbridge.twilio.CapabilityTokenFactory;
import com.twilio.rest.api.v2010.account.Token;
import javax.inject.Inject;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TwilioApi implements TwilioController {

  private final ChatService chatService;
  private final MessageTransformer messageTransformer;
  private final VoiceCallTransformer voiceCallTransformer;
  private final MessageRepository messageRepository;
  private final VoiceCallRepository voiceCallRepository;
  private final TwilioWebsocketClientFactory websocketClientFactory;
  private final CapabilityTokenFactory tokenFactory;

  @Inject
  public TwilioApi(
      @Qualifier("matrixChatService") ChatService chatService,
      MessageTransformer messageTransformer,
      VoiceCallTransformer voiceCallTransformer,
      MessageRepository messageRepository,
      VoiceCallRepository voiceCallRepository,
      TwilioWebsocketClientFactory websocketClientFactory,
      CapabilityTokenFactory tokenFactory) {
    this.chatService = chatService;
    this.messageTransformer = messageTransformer;
    this.voiceCallTransformer = voiceCallTransformer;
    this.messageRepository = messageRepository;
    this.voiceCallRepository = voiceCallRepository;
    this.websocketClientFactory = websocketClientFactory;
    this.tokenFactory = tokenFactory;
  }

  @Override
  public void createSms(TwilioSmsDto sms) {
    log.debug("Received sms: {}", sms);
    Message message = messageTransformer.transform(sms);
    messageRepository.save(message);
    chatService.sendMessage(message);
  }

  @Override
  public ResponseEntity<String> voice(@Valid TwilioVoiceDto voiceDto) {
    log.debug("Received voice call: {}", voiceDto);
    TwilioWebsocketClient client = websocketClientFactory.newClient(
        tokenFactory.getToken("levi-test")
    );
    client.connect();
//
//    VoiceCall voiceCall = voiceCallTransformer.transform(voiceDto);
//    voiceCallRepository.save(voiceCall);
//
//    System.out.println(token.getIceServers());
//    Dial dial = new Dial.Builder()
//        .sip(new Sip.Builder("sip:twilio-user@levimiller-d.sip.us1.twilio.com")
//            .username("twilio-user")
//            .password("Letmein1234567890")
//            .build())
//        .build();
//    try {
//      return ResponseEntity.ok(new VoiceResponse.Builder()
//          .dial(dial)
//          .build()
//          .toXml());
//    } catch (TwiMLException e) {
//      log.error("Error generating dial to sip: ", e);
//      throw new BadRequestException("Error generating dial to sip: ", e);
//    }
    return null;
  }
}
