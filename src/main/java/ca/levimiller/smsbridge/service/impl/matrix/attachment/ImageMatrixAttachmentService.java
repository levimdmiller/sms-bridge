package ca.levimiller.smsbridge.service.impl.matrix.attachment;

import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.Media;
import io.github.ma1uta.matrix.client.AppServiceClient;
import io.github.ma1uta.matrix.event.content.EventContent;
import io.github.ma1uta.matrix.event.message.Image;
import io.github.ma1uta.matrix.event.nested.ImageInfo;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ImageMatrixAttachmentService extends AbstractMatrixAttachmentService {

  @Inject
  public ImageMatrixAttachmentService(AppServiceClient matrixClient) {
    super(matrixClient);
  }

  @Override
  protected EventContent getContent(ChatUser user, Media attachment) {
    Image image = new Image();
    image.setBody("image attachment");
    image.setUrl(uploadFileToMatrix(user, attachment));

    ImageInfo info = new ImageInfo();
    info.setMimetype(attachment.getContentType());
    image.setInfo(info);
    return image;
  }

  @Override
  public boolean supportsType(String contentType) {
    return contentType.startsWith("image");
  }
}
