package ca.levimiller.smsbridge.service.impl.matrix.attachment;

import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.Media;
import ca.levimiller.smsbridge.service.AttachmentService;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class CompositeAttachmentService implements AttachmentService {

  private final AttachmentService defaultAttachmentService;
  private final List<AttachmentService> attachmentServices;

  @Inject
  public CompositeAttachmentService(
      @Qualifier("defaultAttachmentService") AttachmentService defaultAttachmentService,
      List<AttachmentService> attachmentServices) {
    this.defaultAttachmentService = defaultAttachmentService;
    this.attachmentServices = attachmentServices;
  }

  @Override
  public void sendAttachment(ChatUser user, String roomId, Media attachment) {
    AtomicBoolean hasSupportedService = new AtomicBoolean(false);
    attachmentServices.stream()
        .filter(service -> service.supportsType(attachment.getContentType()))
        .forEach(service -> {
          hasSupportedService.set(true);
          service.sendAttachment(user, roomId, attachment);
        });

    // If not sent by anything else, send using default
    if(!hasSupportedService.get()) {
      defaultAttachmentService.sendAttachment(user, roomId, attachment);
    }
  }

  @Override
  public boolean supportsType(String contentType) {
    return true;
  }
}
