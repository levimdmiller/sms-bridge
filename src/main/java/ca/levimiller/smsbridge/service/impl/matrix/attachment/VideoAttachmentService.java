package ca.levimiller.smsbridge.service.impl.matrix.attachment;

import ca.levimiller.smsbridge.data.model.Media;
import io.github.ma1uta.matrix.client.AppServiceClient;
import io.github.ma1uta.matrix.event.content.EventContent;
import io.github.ma1uta.matrix.event.message.Video;
import io.github.ma1uta.matrix.event.nested.VideoInfo;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VideoAttachmentService extends AbstractAttachmentService {

  @Inject
  public VideoAttachmentService(AppServiceClient matrixClient) {
    super(matrixClient);
  }

  @Override
  protected EventContent getContent(Media attachment) {
    Video video = new Video();
    video.setBody("video attachment");
    video.setUrl(attachment.getUrl());

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
