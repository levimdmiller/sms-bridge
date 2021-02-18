package ca.levimiller.smsbridge.service.impl.matrix.attachment;

import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.Media;
import ca.levimiller.smsbridge.service.MatrixAttachmentService;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class CompositeMatrixAttachmentService implements MatrixAttachmentService {

  private final MatrixAttachmentService defaultMatrixAttachmentService;
  private final List<MatrixAttachmentService> matrixAttachmentServices;

  @Inject
  public CompositeMatrixAttachmentService(
      @Qualifier("defaultMatrixAttachmentService")
      MatrixAttachmentService defaultMatrixAttachmentService,
      List<MatrixAttachmentService> matrixAttachmentServices) {
    this.defaultMatrixAttachmentService = defaultMatrixAttachmentService;
    this.matrixAttachmentServices = matrixAttachmentServices;
  }

  @Override
  public void sendAttachment(ChatUser user, String roomId, Media attachment) {
    AtomicBoolean hasSupportedService = new AtomicBoolean(false);
    matrixAttachmentServices.stream()
        .filter(service -> service.supportsType(attachment.getContentType()))
        .forEach(service -> {
          hasSupportedService.set(true);
          service.sendAttachment(user, roomId, attachment);
        });

    // If not sent by anything else, send using default
    if (!hasSupportedService.get()) {
      defaultMatrixAttachmentService.sendAttachment(user, roomId, attachment);
    }
  }

  @Override
  public boolean supportsType(String contentType) {
    return true;
  }
}
