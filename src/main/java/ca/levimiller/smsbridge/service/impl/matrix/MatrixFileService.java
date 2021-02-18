package ca.levimiller.smsbridge.service.impl.matrix;

import ca.levimiller.smsbridge.service.FileService;
import ca.levimiller.smsbridge.util.MxcUri;
import io.github.ma1uta.matrix.client.AppServiceClient;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Qualifier("matrixFileService")
public class MatrixFileService implements FileService {
  private final AppServiceClient matrixClient;

  @Inject
  public MatrixFileService(AppServiceClient matrixClient) {
    this.matrixClient = matrixClient;
  }

  @Override
  public InputStream getFileStream(String url) throws URISyntaxException, IOException {
    MxcUri uri = MxcUri.of(url);
    try {
      return matrixClient.content()
          .download(uri.getServerName(), uri.getMediaId(), true)
          .join();
    } catch (CancellationException | CompletionException error) {
      throw new IOException("Error fetching media from matrix server: " + url, error);
    }
  }
}
