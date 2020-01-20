package ca.levimiller.smsbridge.service;

import ca.levimiller.smsbridge.data.model.Message;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

/**
 * interface for communicating with a chat server (matrix, discord, etc.)
 */
@Validated
public interface ChatService {

  /**
   * Sends the given message to the chat server.
   * @param message - message to send.
   */
  void sendMessage(@Valid @NotNull Message message);
}
