package ca.levimiller.smsbridge.service.impl.matrix.attachment;

import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.Media;
import ca.levimiller.smsbridge.service.FileService;
import io.github.ma1uta.matrix.client.AppServiceClient;
import io.github.ma1uta.matrix.event.content.EventContent;
import io.github.ma1uta.matrix.event.message.File;
import io.github.ma1uta.matrix.event.nested.FileInfo;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

@Service
public class FileMatrixAttachmentService extends AbstractMatrixAttachmentService {

  @Inject
  public FileMatrixAttachmentService(FileService fileService, AppServiceClient matrixClient) {
    super(fileService, matrixClient);
  }

  @Override
  protected EventContent getContent(ChatUser user, Media attachment) {
    File file = new File();
    file.setBody("file attachment");
    file.setUrl(uploadFileToMatrix(user, attachment));

    FileInfo info = new FileInfo();
    info.setMimetype(attachment.getContentType());
    file.setInfo(info);
    return file;
  }

  @Override
  public boolean supportsType(String contentType) {
    return contentType.startsWith("application");
  }
}
