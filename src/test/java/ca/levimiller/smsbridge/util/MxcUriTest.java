package ca.levimiller.smsbridge.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;

public class MxcUriTest {
  @Test
  void null_throwsURISyntaxException() {
    URISyntaxException thrown = assertThrows(
        URISyntaxException.class,
        () -> MxcUri.of(null),
        "Expected MxcUri.of(null) to throw an exception"
    );

    assertTrue(thrown.getMessage().contains("Uri string is null"));
  }

  @Test
  void badScheme_throwsURISyntaxException() {
    URISyntaxException thrown = assertThrows(
        URISyntaxException.class,
        () -> MxcUri.of("http://serverName/mediaId"),
        "Expected MxcUri.of() to throw an exception"
    );

    assertTrue(thrown.getMessage().contains("Uri protocol isn't mxc"));
  }

  @Test
  void onePart_throwsURISyntaxException() {
    URISyntaxException thrown = assertThrows(
        URISyntaxException.class,
        () -> MxcUri.of("mxc://serverName"),
        "Expected MxcUri.of() to throw an exception"
    );

    assertTrue(thrown.getMessage().contains("Uri string should be exactly 2 parts: <server-name>/<media-id>"));
  }

  @Test
  void threePart_throwsURISyntaxException() {
    URISyntaxException thrown = assertThrows(
        URISyntaxException.class,
        () -> MxcUri.of("mxc://serverName/mediaId/junk"),
        "Expected MxcUri.of() to throw an exception"
    );

    assertTrue(thrown.getMessage().contains("Uri string should be exactly 2 parts: <server-name>/<media-id>"));
  }

  @Test
  void validUri_shouldParse() throws URISyntaxException {
    MxcUri uri = MxcUri.of("mxc://serverName/mediaId");

    assertEquals("serverName", uri.getServerName());
    assertEquals("mediaId", uri.getMediaId());
  }
}
