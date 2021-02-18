package ca.levimiller.smsbridge.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

public interface FileService {

  /**
   * Gets the file at the given url.
   * @param url - file to fetch
   * @return - file stream
   * @throws URISyntaxException - if url is invalid
   * @throws IOException - if an io exception occurs
   */
  InputStream getFileStream(String url) throws URISyntaxException, IOException;
}
