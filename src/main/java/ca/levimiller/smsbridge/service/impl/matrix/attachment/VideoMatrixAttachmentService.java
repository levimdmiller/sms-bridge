package ca.levimiller.smsbridge.service.impl.matrix.attachment;

import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.Media;
import ca.levimiller.smsbridge.service.FileService;
import io.github.ma1uta.matrix.client.AppServiceClient;
import io.github.ma1uta.matrix.event.content.EventContent;
import io.github.ma1uta.matrix.event.message.Video;
import io.github.ma1uta.matrix.event.nested.VideoInfo;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VideoMatrixAttachmentService extends AbstractMatrixAttachmentService {

  @Inject
  public VideoMatrixAttachmentService(FileService fileService, AppServiceClient matrixClient) {
    super(fileService, matrixClient);
  }

  @Override
  protected EventContent getContent(ChatUser user, Media attachment) {
    Video video = new Video();
    video.setBody("video attachment");
    video.setUrl(uploadFileToMatrix(user, attachment));

    VideoInfo info = new VideoInfo();
    info.setMimetype(attachment.getContentType());
    video.setInfo(info);
    return video;
  }


  @Override
  public boolean supportsType(String contentType) {
    return contentType.startsWith("video");
  }
}
