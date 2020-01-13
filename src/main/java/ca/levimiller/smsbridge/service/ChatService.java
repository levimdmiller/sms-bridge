package ca.levimiller.smsbridge.service;

import ca.levimiller.smsbridge.data.model.Message;

/**
 * interface for communicating with a chat server (matrix, discord, etc.)
 */
public interface ChatService {

  /**
   * Sends the given message to the chat server.
   * @param message - message to send.
   */
  void sendMessage(Message message);
}
