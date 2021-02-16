package ca.levimiller.smsbridge.service;

import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.Media;

public interface AttachmentService {

  /**
   * Sends the given attachment.
   * @param attachment - attachment to send
   */
  void sendAttachment(ChatUser user, String roomId, Media attachment);

  /**
   * Returns true if the given content type is supported by the attachment service.
   * @param contentType - content type to check
   * @return - true if supported
   */
  boolean supportsType(String contentType);
}
