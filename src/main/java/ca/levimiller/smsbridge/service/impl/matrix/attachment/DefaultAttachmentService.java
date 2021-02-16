package ca.levimiller.smsbridge.service.impl.matrix.attachment;

import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.Media;
import ca.levimiller.smsbridge.service.AttachmentService;
import io.github.ma1uta.matrix.client.AppServiceClient;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Qualifier("defaultAttachmentService")
public class DefaultAttachmentService implements AttachmentService {
  private final AppServiceClient matrixClient;

  @Inject
  public DefaultAttachmentService(AppServiceClient matrixClient) {
    this.matrixClient = matrixClient;
  }

  @Override
  public void sendAttachment(ChatUser user, String roomId, Media attachment) {
    String messageBody = String.format(
        "%s - %s",
        attachment.getContentType(),
        attachment.getUrl());

    matrixClient.userId(user.getOwnerId()).event().sendMessage(roomId, messageBody)
        .exceptionally(throwable -> {
          log.error("Error sending attachment to matrix (DefaultAttachmentService): ", throwable);
          return null;
        });
  }

  @Override
  public boolean supportsType(String contentType) {
    return false;
  }
}
