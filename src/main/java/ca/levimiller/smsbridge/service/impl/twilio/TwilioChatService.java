package ca.levimiller.smsbridge.service.impl.twilio;

import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.service.ChatService;
import ca.levimiller.smsbridge.twilio.MessageCreatorFactory;
import com.twilio.type.PhoneNumber;
import java.util.Objects;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("twilioChatService")
@Slf4j
public class TwilioChatService implements ChatService {
  private final MessageCreatorFactory messageCreatorFactory;

  @Inject
  public TwilioChatService(
      MessageCreatorFactory messageCreatorFactory) {
    this.messageCreatorFactory = messageCreatorFactory;
  }

  @Override
  public void sendMessage(Message message) {
    Objects.requireNonNull(message.getFromContact(), "Origin contact must not be null.");
    Objects.requireNonNull(message.getToContact(), "Destination contact must not be null.");

    // send sms
    messageCreatorFactory.getCreator(
        new PhoneNumber(message.getToContact().getNumber()),
        new PhoneNumber(message.getFromContact().getNumber()),
        message.getBody()
    ).create();
  }
}
