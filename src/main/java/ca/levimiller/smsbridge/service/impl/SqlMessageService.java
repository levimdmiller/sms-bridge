package ca.levimiller.smsbridge.service.impl;

import ca.levimiller.smsbridge.data.db.MessageRepository;
import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SqlMessageService implements MessageService {

  private final MessageRepository messageRepository;

  @Autowired
  public SqlMessageService(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  @Override
  public Message save(Message message) {
    return messageRepository.save(message);
  }
}
