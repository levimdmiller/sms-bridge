package ca.levimiller.smsbridge.service;

import ca.levimiller.smsbridge.data.model.Message;

/**
 * interface for communicating with a chat server (matrix, discord, etc.)
 */
public interface ChatService {

  void sendMessage(Message message);
}
