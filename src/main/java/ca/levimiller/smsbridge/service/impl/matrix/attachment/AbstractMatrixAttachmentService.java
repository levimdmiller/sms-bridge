package ca.levimiller.smsbridge.service.impl.matrix.attachment;

import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.Media;
import ca.levimiller.smsbridge.service.FileService;
import ca.levimiller.smsbridge.service.MatrixAttachmentService;
import io.github.ma1uta.matrix.client.AppServiceClient;
import io.github.ma1uta.matrix.event.content.EventContent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractMatrixAttachmentService implements MatrixAttachmentService {
  private final FileService fileService;
  protected final AppServiceClient matrixClient;

  public AbstractMatrixAttachmentService(
      FileService fileService,
      AppServiceClient matrixClient) {
    this.fileService = fileService;
    this.matrixClient = matrixClient;
  }

  /**
   * Builds the event content from the attachment.
   * @param attachment - attachment to send
   * @return - event content for attachment
   */
  protected abstract EventContent getContent(ChatUser user, Media attachment);

  @Override
  public void sendAttachment(ChatUser user, String roomId, Media attachment) {
    matrixClient.userId(user.getOwnerId()).event()
        .sendEvent(roomId, "m.room.message", getContent(user, attachment))
        .exceptionally(throwable -> {
          log.error(
              String.format("Error sending attachment to matrix (%s): ", getClass()),
              throwable);
          return null;
        });
  }

  /**
   * Tries to upload the file at the given url to the matrix homeserver.
   *  <p></p>
   * If an exception occurs during the upload, the original url is returned.
   * @param user - user uploading the file
   * @param media - file to upload
   * @return - new url from matrix
   */
  protected String uploadFileToMatrix(ChatUser user, Media media) {
    try {
      String fileName = media.getUrl().substring(media.getUrl().lastIndexOf("/") + 1);

      // Download file
      InputStream source = fileService.getFileStream(media.getUrl());

      return matrixClient.userId(user.getOwnerId()).content()
          .upload(source, fileName, media.getContentType())
          .join();
    } catch (IOException | URISyntaxException | CancellationException | CompletionException error) {
      log.error(
          String.format("Error uploading file to matrix homeserver (%s): ", getClass()),
          error);
      // Fallback to initial url, so matrix message is sent instead of just a twilio error.
      return media.getUrl();
    }
  }
}
