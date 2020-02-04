package ca.levimiller.smsbridge.matrix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.ma1uta.matrix.client.RequestParams;
import javax.ws.rs.client.WebTarget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppJaxRsRequestFactoryTest {

  private AppJaxRsRequestFactory appJaxRsRequestFactory;
  private String homeserverUrl;
  private RequestParams requestParams;
  private WebTarget webTarget;
  private WebTarget modifiedWebTarget;

  @BeforeEach
  void setUp() {
    homeserverUrl = "https://test.ca";
    appJaxRsRequestFactory = new AppJaxRsRequestFactory(homeserverUrl);
    requestParams = new RequestParams()
        .accessToken("access-token")
        .deviceId("device-id");
    webTarget = mock(WebTarget.class);
    when(webTarget.queryParam(any(), any())).thenReturn(modifiedWebTarget);
  }

  @Test
  void testNullUserId() {
    requestParams.userId(null);
    WebTarget result = appJaxRsRequestFactory.applyQueryParams(requestParams, webTarget);
    assertEquals(webTarget, result);
    verify(webTarget, times(0)).queryParam(any(), any());
  }

  @Test
  void test_UserId() {
    requestParams.userId("user-id");
    WebTarget result = appJaxRsRequestFactory.applyQueryParams(requestParams, webTarget);
    assertEquals(modifiedWebTarget, result);
    verify(webTarget).queryParam("user_id", "user-id");
  }
}
