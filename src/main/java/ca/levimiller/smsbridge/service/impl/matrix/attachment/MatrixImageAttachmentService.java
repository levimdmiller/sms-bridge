package ca.levimiller.smsbridge.service.impl.matrix.attachment;

import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.Media;
import ca.levimiller.smsbridge.service.AttachmentService;

public class MatrixImageAttachmentService implements AttachmentService {
  @Override
  public void sendAttachment(ChatUser user, String roomId, Media attachment) {

  }

  @Override
  public boolean supportsType(String contentType) {
    return false;
  }
}
