package ca.levimiller.smsbridge.service;

import ca.levimiller.smsbridge.data.model.Media;

public interface OutgoingAttachmentService {

  /**
   * Sends the given attachment to matrix.
   * @param attachment - attachment to send
   */
  void sendAttachment(Media attachment);
}
