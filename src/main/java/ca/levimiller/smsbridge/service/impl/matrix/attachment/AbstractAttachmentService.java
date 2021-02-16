package ca.levimiller.smsbridge.service.impl.matrix.attachment;

import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.Media;
import ca.levimiller.smsbridge.service.AttachmentService;
import io.github.ma1uta.matrix.client.AppServiceClient;
import io.github.ma1uta.matrix.event.content.EventContent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractAttachmentService implements AttachmentService {
  protected final AppServiceClient matrixClient;

  public AbstractAttachmentService(AppServiceClient matrixClient) {
    this.matrixClient = matrixClient;
  }

  /**
   * Builds the event content from the attachment.
   * @param attachment - attachment to send
   * @return - event content for attachment
   */
  protected abstract EventContent getContent(Media attachment);

  @Override
  public void sendAttachment(ChatUser user, String roomId, Media attachment) {
    matrixClient.userId(user.getOwnerId()).event()
        .sendEvent(roomId, "m.room.message", getContent(attachment))
        .exceptionally(throwable -> {
          log.error(
              String.format("Error sending attachment to matrix (%s): ", getClass()),
              throwable);
          return null;
        });
  }
}
