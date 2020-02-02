package ca.levimiller.smsbridge.service.impl.twilio;

import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.service.ChatService;
import ca.levimiller.smsbridge.twilio.MessageCreatorFactory;
import com.twilio.type.PhoneNumber;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Qualifier("twilioChatService")
public class TwilioChatService implements ChatService {

  private final MessageCreatorFactory messageCreatorFactory;

  @Inject
  public TwilioChatService(
      MessageCreatorFactory messageCreatorFactory) {
    this.messageCreatorFactory = messageCreatorFactory;
  }

  @Override
  public void sendMessage(Message message) {
    // send sms
    messageCreatorFactory.getCreator(
        new PhoneNumber(message.getToContact().getNumber()),
        new PhoneNumber(message.getFromContact().getNumber()),
        message.getBody()
    ).create();
  }
}
