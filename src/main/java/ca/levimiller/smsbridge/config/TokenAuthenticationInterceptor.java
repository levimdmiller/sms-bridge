package ca.levimiller.smsbridge.config;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class TokenAuthenticationInterceptor implements ClientHttpRequestInterceptor {
  private final String token;

  public TokenAuthenticationInterceptor(String token) {
    this.token = token;
  }

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body,
      ClientHttpRequestExecution execution) throws IOException {
    HttpHeaders headers = request.getHeaders();
    if (!headers.containsKey("Authorization")) {
      headers.setBasicAuth(token);
    }

    return execution.execute(request, body);
  }
}
