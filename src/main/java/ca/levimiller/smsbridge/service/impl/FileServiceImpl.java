package ca.levimiller.smsbridge.service.impl;

import ca.levimiller.smsbridge.service.FileService;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@Qualifier("simpleFileService")
public class FileServiceImpl implements FileService {

  @Override
  public InputStream getFileStream(String url) throws URISyntaxException, IOException {
    // Download file
    CloseableHttpClient httpclient = HttpClients.custom()
        .setRedirectStrategy(new LaxRedirectStrategy())
        .build();
    HttpGet get = new HttpGet(new URL(url).toURI());
    HttpResponse response = httpclient.execute(get);
    return response.getEntity().getContent();
  }
}
