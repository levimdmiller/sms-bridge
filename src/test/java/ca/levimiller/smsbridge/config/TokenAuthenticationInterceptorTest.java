package ca.levimiller.smsbridge.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.mock.http.client.MockClientHttpRequest;

class TokenAuthenticationInterceptorTest {
  private ClientHttpRequestInterceptor httpRequestInterceptor;
  private ClientHttpRequestExecution mockExecution;

  @BeforeEach
  void setUp() {
    httpRequestInterceptor = new TokenAuthenticationInterceptor("token");
    mockExecution = mock(ClientHttpRequestExecution.class);
  }

  @Test
  void intercept() throws IOException {
    byte[] body = new byte[0];
    HttpRequest request = new MockClientHttpRequest();

    ArgumentCaptor<HttpRequest> args = ArgumentCaptor.forClass(HttpRequest.class);
    httpRequestInterceptor.intercept(request, body, mockExecution);
    verify(mockExecution, times(1)).execute(args.capture(), eq(body));
    assertEquals("/?access_token=token", args.getValue().getURI().toString());
  }
}
