package ca.levimiller.smsbridge.service;

import ca.levimiller.smsbridge.data.model.Message;

public interface MessageService {

  /**
   * Saves the given message.
   *
   * @param message - message to save
   * @return - updated message
   */
  Message save(Message message);
}
