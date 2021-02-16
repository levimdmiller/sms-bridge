package ca.levimiller.smsbridge.service.impl.matrix.attachment;

import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.Media;
import io.github.ma1uta.matrix.client.AppServiceClient;
import io.github.ma1uta.matrix.event.content.EventContent;
import io.github.ma1uta.matrix.event.message.Audio;
import io.github.ma1uta.matrix.event.nested.AudioInfo;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

@Service
public class AudioMatrixAttachmentService extends AbstractMatrixAttachmentService {

  @Inject
  public AudioMatrixAttachmentService(AppServiceClient matrixClient) {
    super(matrixClient);
  }

  @Override
  protected EventContent getContent(ChatUser user, Media attachment) {
    Audio audio = new Audio();
    audio.setBody("audio attachment");
    audio.setUrl(uploadFileToMatrix(user, attachment));

    AudioInfo info = new AudioInfo();
    info.setMimetype(attachment.getContentType());
    audio.setInfo(info);
    return audio;
  }

  @Override
  public boolean supportsType(String contentType) {
    return contentType.startsWith("audio");
  }
}
