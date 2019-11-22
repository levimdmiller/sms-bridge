package ca.levimiller.smsbridge.service.impl;

import ca.levimiller.smsbridge.data.db.ContactRepository;
import ca.levimiller.smsbridge.data.db.MessageRepository;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.service.ChatService;
import ca.levimiller.smsbridge.service.MessageService;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

@Component
public class SqlMessageService implements MessageService {
  private final ContactRepository contactRepository;
  private final MessageRepository messageRepository;
  private final ChatService chatService;

  @Inject
  public SqlMessageService(ContactRepository contactRepository,
      MessageRepository messageRepository, ChatService chatService) {
    this.contactRepository = contactRepository;
    this.messageRepository = messageRepository;
    this.chatService = chatService;
  }

  @Override
  public Message save(Message message) {
    message.setToContact(findContact(message.getToContact()));
    message.setFromContact(findContact(message.getFromContact()));
    Message savedMessage = messageRepository.save(message);
    chatService.sendMessage(savedMessage);
    return savedMessage;
  }

  private Contact findContact(Contact contact) {
    return contactRepository.findDistinctByNumber(contact.getNumber())
        .orElse(contact);
  }
}
