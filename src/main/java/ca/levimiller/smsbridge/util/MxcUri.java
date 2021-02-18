package ca.levimiller.smsbridge.util;

import java.net.URISyntaxException;
import lombok.Data;

@Data
public class MxcUri {
  private final String serverName;
  private final String mediaId;

  /**
   * Parses string to a mxc uri.
   * @param mxcUri - string to parse
   * @return - MxcUri
   * @throws URISyntaxException if the parsing fails
   */
  public static MxcUri of(String mxcUri) throws URISyntaxException {
    if (mxcUri == null) {
      throw new URISyntaxException("null", "Uri string is null");
    }
    if (!mxcUri.startsWith("mxc://")) {
      throw new URISyntaxException(mxcUri, "Uri protocol isn't mxc");
    }

    String[] parts = mxcUri.substring(6).split("/");
    if (parts.length != 2) {
      throw new URISyntaxException(
          mxcUri, "Uri string should be exactly 2 parts: <server-name>/<media-id>");
    }

    return new MxcUri(parts[0], parts[1]);
  }
}
