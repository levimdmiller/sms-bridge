package ca.levimiller.smsbridge.service.impl;

import ca.levimiller.smsbridge.data.db.MessageRepository;
import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.service.MessageService;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

@Component
public class SqlMessageService implements MessageService {

  private final MessageRepository messageRepository;

  @Inject
  public SqlMessageService(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  @Override
  public Message save(Message message) {
    return messageRepository.save(message);
  }
}
